package de.aklingauf.qrcodehelper.controller;

import de.aklingauf.qrcodehelper.exception.ResourceNotFoundException;
import de.aklingauf.qrcodehelper.model.QRCode;
import de.aklingauf.qrcodehelper.model.QRRedirect;
import de.aklingauf.qrcodehelper.repository.QRCodeRepository;
import de.aklingauf.qrcodehelper.security.CurrentUser;
import de.aklingauf.qrcodehelper.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8100")
@RequestMapping("/api")
public class QRCodeController {
    @Autowired
    QRCodeRepository qrCodeRepository;

    // Get

    @GetMapping("/qrcodes/all")
    public List<QRCode> getAllQRCodes(){
        return qrCodeRepository.findAll();
    }

    @GetMapping("/qrcodes/{qrCodeId}")
    public QRCode getQRCode(@PathVariable (value = "qrCodeId") Long qrCodeId){
        return qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("QRCode", "qrCodeId", qrCodeId));
    }

    // Post
    @PostMapping("/qrcodes/")
    public QRCode createQRCode(){
        return qrCodeRepository.save(new QRCode());
    }

    @PostMapping("qrcodes/create")
    public ArrayList<Long> createManyQRCodes(@RequestParam (name = "count") Long count){
        ArrayList<Long> ids = new ArrayList<>();
        for(int i = 1; i <= count; i++){
            QRCode newQR = new QRCode();
            ids.add(newQR.getId());
        }
        return ids;
    }


}
