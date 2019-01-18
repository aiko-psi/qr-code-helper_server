package de.aklingauf.qrcodehelper.controller;

import de.aklingauf.qrcodehelper.exception.ResourceNotFoundException;
import de.aklingauf.qrcodehelper.model.QRRedirect;
import de.aklingauf.qrcodehelper.payload.ApiResponse;
import de.aklingauf.qrcodehelper.repository.QRCodeRepository;
import de.aklingauf.qrcodehelper.repository.QRRedirectRepository;
import de.aklingauf.qrcodehelper.repository.UserRepository;
import de.aklingauf.qrcodehelper.security.CurrentUser;
import de.aklingauf.qrcodehelper.security.UserPrincipal;
import de.aklingauf.qrcodehelper.validator.OwnValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8100")
@RequestMapping("/api")
public class QRRedirectController {

    @Autowired
    QRRedirectRepository qrRedirectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QRCodeRepository qrCodeRepository;

    OwnValidator validator;

    // Get

    @GetMapping("/qrredirects/all")
    public List<QRRedirect> getAllQRRedirects(){
        return qrRedirectRepository.findAll();
    }

    @GetMapping("/qrredirects/user")
    public List<QRRedirect> getQRRedirectsUser(@CurrentUser UserPrincipal currentUser){
        return qrRedirectRepository.findByOwnerId(currentUser.getId());
    }

    @GetMapping("/qrredirects/{redirectId}")
    public QRRedirect getQRRedirectById(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable (value = "redirectId") Long redirectId){
        return qrRedirectRepository.findById(redirectId)
                .orElseThrow(() -> new ResourceNotFoundException("QRRedirect", "redirectId", redirectId));
    }

    // Post

    @PostMapping("/qrredirects/{qrCodeId}")
    public QRRedirect createQRRedirect(@CurrentUser UserPrincipal currentUser,
                                       @PathVariable (value = "qrCodeId") Long qrCodeId,
                                       @Valid @RequestBody QRRedirect qrRedirect) throws IOException {
        validator.checkCreationPossible(qrCodeId);
        validator.checkURLString(qrRedirect.getAddress());

        userRepository.findById(currentUser.getId()).map(user -> {
            qrRedirect.setOwner(user);
            return qrRedirect;
        }).orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        qrCodeRepository.findById(qrCodeId).map(qrCode -> {
            qrRedirect.setQrCode(qrCode);
            return qrRedirect;
        });

        return qrRedirectRepository.save(qrRedirect);
    }

    // Put

    @PutMapping("/qrredirects/{redirectId}")
    public QRRedirect updateQRRedirect(@CurrentUser UserPrincipal currentUser,
                                       @PathVariable (value = "redirectId") Long redirectId,
                                       @Valid @RequestBody QRRedirect qrRedirectRequest) throws IOException{
        validator.checkChangeAllowed(redirectId, currentUser);
        validator.checkURLString(qrRedirectRequest.getAddress());

        return qrRedirectRepository.findById(redirectId).map(qrRedirect -> {
            qrRedirect.setTitel(qrRedirectRequest.getTitel());
            qrRedirect.setAddress(qrRedirectRequest.getAddress());
            qrRedirect.setLocation(qrRedirectRequest.getLocation());
            qrRedirect.setOpen(qrRedirectRequest.isOpen());
            return qrRedirectRepository.save(qrRedirect);
        }).orElseThrow(() -> new ResourceNotFoundException("QRRedirect", "redirectId", redirectId));
    }

    // Delete
    @DeleteMapping("/qrredirects/{redirectId}")
    public ResponseEntity<?> deleteQRRedirect(@PathVariable (value = "redirectId") Long redirectId){
        return qrRedirectRepository.findById(redirectId).map(qrRedirect -> {
            qrRedirectRepository.delete(qrRedirect);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("QRRedirect", "redirectId", redirectId));
    }

    // Check
    @GetMapping("/qrredirects/check/{redirectId}")
    public ResponseEntity<?> checkQRRedirect(@PathVariable (value = "redirectId") Long redirectId){
        if(qrRedirectRepository.existsById(redirectId)){
            return new ResponseEntity(new ApiResponse(false, "QR-Code schon belegt!"),
                    HttpStatus.resolve(200));
        }else{
            return new ResponseEntity(new ApiResponse(true, ""),
                    HttpStatus.resolve(200));
        }
    }


}
