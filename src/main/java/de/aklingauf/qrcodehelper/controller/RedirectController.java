package de.aklingauf.qrcodehelper.controller;

import de.aklingauf.qrcodehelper.exception.RedirectionError;
import de.aklingauf.qrcodehelper.exception.ResourceNotFoundException;
import de.aklingauf.qrcodehelper.model.QRRedirect;
import de.aklingauf.qrcodehelper.repository.QRCodeRepository;
import de.aklingauf.qrcodehelper.repository.QRRedirectRepository;
import de.aklingauf.qrcodehelper.security.CurrentUser;
import de.aklingauf.qrcodehelper.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/qrcodehelper/")
public class RedirectController {
    @Autowired
    QRRedirectRepository qrRedirectRepository;

    @Autowired
    QRCodeRepository qrCodeRepository;

    @GetMapping("/qrredirect/{qrCodeId}")
    public ModelAndView redirect(@PathVariable(value = "qrCodeId") Long qrCodeId){

        if(!qrRedirectRepository.existsById(qrCodeId)){
            new RedirectionError();
        }

        return qrCodeRepository.findById(qrCodeId).map(qrCode -> {
            String address;
            if(qrCode.getRedirect().getAddress().matches("https(.*)")){
                address = "redirect:" +  qrCode.getRedirect().getAddress();
            } else {
                address = "redirect:https://" + qrCode.getRedirect().getAddress();
            }
            return new ModelAndView(address);
        }).orElseThrow(() -> new ResourceNotFoundException("QRCode", "qrCodeId", qrCodeId));

    }


}
