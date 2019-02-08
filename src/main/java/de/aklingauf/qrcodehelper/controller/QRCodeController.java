package de.aklingauf.qrcodehelper.controller;

import de.aklingauf.qrcodehelper.exception.ResourceNotFoundException;
import de.aklingauf.qrcodehelper.model.QRCode;
import de.aklingauf.qrcodehelper.model.QRRedirect;
import de.aklingauf.qrcodehelper.payload.ApiResponse;
import de.aklingauf.qrcodehelper.payload.QRListResponse;
import de.aklingauf.qrcodehelper.repository.QRCodeRepository;
import de.aklingauf.qrcodehelper.security.CurrentUser;
import de.aklingauf.qrcodehelper.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //Check

    @GetMapping("/qrcodes/check/exist/{qrCodeId}")
    public ResponseEntity<?> checkQRExists(@PathVariable (value = "qrCodeId") Long qrCodeId){
        if(qrCodeRepository.existsById(qrCodeId)){
            return new ResponseEntity(new ApiResponse(true, "QR-Code verfügbar."),
                    HttpStatus.resolve(200));
        }else{
            return new ResponseEntity(new ApiResponse(false, ""),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/qrcodes/check/filled/{qrCodeId}")
    public ResponseEntity<?> checkQRFilled(@PathVariable (value = "qrCodeId") Long qrCodeId){
        return qrCodeRepository.findById(qrCodeId).map(qrCode -> {
            if(qrCode.getRedirect() != null){
                return new ResponseEntity(new ApiResponse(true, "QR-Code belegt."),
                        HttpStatus.resolve(200));
            }else{
                return new ResponseEntity(new ApiResponse(false, "QR-Code nicht belegt."),
                        HttpStatus.BAD_REQUEST);
            }
        }).orElseThrow(() -> new ResourceNotFoundException("QRCode", "qrCodeId", qrCodeId));
    }


    // Post
    @PostMapping("/qrcodes")
    public QRCode createQRCode(){
        QRCode qr = new QRCode();
        return qrCodeRepository.save(qr);
    }

    @PostMapping("qrcodes/create")
    public QRListResponse createManyQRCodes(@RequestParam (name = "count") Long count){
        ArrayList<Long> ids = new ArrayList<>();
        for(int i = 1; i <= count; i++){
            QRCode newQR = new QRCode();
            qrCodeRepository.save(newQR);
            ids.add(newQR.getId());
        }
        return new QRListResponse(true, "Success", ids);
    }


}
