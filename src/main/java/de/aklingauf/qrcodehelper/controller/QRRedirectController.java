package de.aklingauf.qrcodehelper.controller;

import de.aklingauf.qrcodehelper.exception.ResourceNotFoundException;
import de.aklingauf.qrcodehelper.model.QRRedirect;
import de.aklingauf.qrcodehelper.repository.QRRedirectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8100")
@RequestMapping("/api")
public class QRRedirectController {

    @Autowired
    QRRedirectRepository qrRedirectRepository;

    // Get

    @GetMapping("/qrredirects/all")
    public List<QRRedirect> getAllQRRedirects(){
        return qrRedirectRepository.findAll();
    }

    @GetMapping("/qrredirects/user/{userId}")
    public List<QRRedirect> getQRRedirectsUser(@PathVariable (value = "userId") Long userId){
        return qrRedirectRepository.findByOwnerId(userId);
    }

    @GetMapping("/qrredirects/{redirectId}")
    public QRRedirect getQRRedirectById(@PathVariable (value = "redirectId") Long redirectId){
        return qrRedirectRepository.findById(redirectId)
                .orElseThrow(() -> new ResourceNotFoundException("QRRedirect", "redirectId", redirectId));
    }

    // Post

    @PostMapping("/qrredirects")
    public QRRedirect createQRRedirect(@Valid @RequestBody QRRedirect qrRedirect){
        return qrRedirectRepository.save(qrRedirect);
    }

    // Put

    @PutMapping("/qrredirects/{redirectId}")
    public QRRedirect updateQRRedirect(@PathVariable (value = "redirectId") Long redirectId,
                                       @Valid @RequestBody QRRedirect qrRedirectRequest){
        return qrRedirectRepository.findById(redirectId).map(qrRedirect -> {
            qrRedirect.setTitel(qrRedirectRequest.getTitel());
            qrRedirect.setAddress(qrRedirectRequest.getAddress());
            qrRedirect.setLocation(qrRedirectRequest.getLocation());
            qrRedirect.setOpen(qrRedirectRequest.isOpen());
            return qrRedirectRepository.save(qrRedirect);
        }).orElseThrow(() -> new ResourceNotFoundException("QRRedirect", "redirectId", redirectId));
    }

    // Delete
    @DeleteMapping("/qrredirects/{redirectId")
    public ResponseEntity<?> deleteQRRedirect(@PathVariable (value = "redirectId") Long redirectId){
        return qrRedirectRepository.findById(redirectId).map(qrRedirect -> {
            qrRedirectRepository.delete(qrRedirect);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("QRRedirect", "redirectId", redirectId));
    }


}
