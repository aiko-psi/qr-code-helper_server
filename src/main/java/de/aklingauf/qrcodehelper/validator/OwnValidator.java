package de.aklingauf.qrcodehelper.validator;

import com.sun.deploy.util.URLUtil;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import de.aklingauf.qrcodehelper.exception.BadRequestException;
import de.aklingauf.qrcodehelper.exception.ResourceNotFoundException;
import de.aklingauf.qrcodehelper.model.QRRedirect;
import de.aklingauf.qrcodehelper.repository.QRCodeRepository;
import de.aklingauf.qrcodehelper.repository.QRRedirectRepository;
import de.aklingauf.qrcodehelper.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;

@Service
public class OwnValidator {
    @Autowired
    QRRedirectRepository qrRedirectRepository;

    @Autowired
    QRCodeRepository qrCodeRepository;

    public void checkChangeAllowed(Long redirectId, UserPrincipal currentUser) throws BadRequestException, ResourceNotFoundException{
        qrRedirectRepository.findById(redirectId).map(redirect -> {
            if(!redirect.isOpen() && !redirect.getOwnerId().equals(currentUser.getId())){
                System.out.println("User Permission denied");
                throw new BadRequestException("User has no permission to change this object");
            }
            System.out.println("User Permission ok");
            return redirect;
        }).orElseThrow(() -> new ResourceNotFoundException("QRRedirect", "redirectId", redirectId));
    }

    public void checkCreationPossible(Long qrCodeId) throws BadRequestException{
        if(!qrCodeRepository.existsById(qrCodeId)){
            throw new BadRequestException("QR Code not existing.");
        }
    }

    public void checkURLString(String address) throws IOException, BadRequestException {
        if(isValidURL(address)){
            this.doesURLExist(address);
        }else if (isValidURL("https://" + address)){
            this.doesURLExist("https://" + address);
        }else{
            throw new BadRequestException("URL does not exist.");
        }
    }
    public boolean isValidURL(String address){
        try {
            URL url = new URL(address);
            return true;
        } catch (MalformedURLException e){
            return false;
        }
    }

    // from: https://stackoverflow.com/a/45036771 (changed)
    public void doesURLExist(String address) throws IOException, BadRequestException {
        URL url = new URL(address);

        // We want to check the current URL
        HttpURLConnection.setFollowRedirects(false);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // We don't need to get data
        httpURLConnection.setRequestMethod("HEAD");

        // Some websites don't like programmatic access so pretend to be a browser
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; " +
                "rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        int responseCode = httpURLConnection.getResponseCode();

        // We only accept response code 200
        if(responseCode != HttpURLConnection.HTTP_OK &&
                responseCode != HttpURLConnection.HTTP_FORBIDDEN &&
                responseCode != HttpURLConnection.HTTP_SEE_OTHER){
            throw new BadRequestException("URL does not exist." + responseCode);
        }
    }


}
