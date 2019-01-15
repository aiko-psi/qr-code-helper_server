package de.aklingauf.qrcodehelper.controller;

import de.aklingauf.qrcodehelper.exception.ResourceNotFoundException;
import de.aklingauf.qrcodehelper.model.QRRedirect;
import de.aklingauf.qrcodehelper.repository.QRRedirectRepository;
import de.aklingauf.qrcodehelper.security.CurrentUser;
import de.aklingauf.qrcodehelper.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin(origins = "http://localhost:8100")
@RequestMapping("/qrcodehelper/")
public class RedirectController {
    @Autowired
    QRRedirectRepository qrRedirectRepository;

    @GetMapping("/qrredirect/{redirectId}")
    public ModelAndView redirect(@PathVariable(value = "redirectId") Long redirectId){

        if(!qrRedirectRepository.existsById(redirectId)){
            ModelAndView errorPage = new ModelAndView("NoRedirectionError");
            errorPage.addObject("errorMsg", "Es ist noch keine Weiterleitung zu " +
                    "diesem QR-Code hinterlegt.");
            return errorPage;
        }

        return qrRedirectRepository.findById(redirectId).map(redirect ->{
            String address = "redirect:https://" + redirect.getAddress();
            return new ModelAndView(address);
        }).orElseThrow(() -> new ResourceNotFoundException("QRRedirect", "redirectId", redirectId));

    }


}
