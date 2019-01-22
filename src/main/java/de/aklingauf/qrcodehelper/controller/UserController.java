package de.aklingauf.qrcodehelper.controller;

import de.aklingauf.qrcodehelper.exception.ResourceNotFoundException;
import de.aklingauf.qrcodehelper.model.User;
import de.aklingauf.qrcodehelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @GetMapping("username/{username}/{email}")
    public User getUserDetails(@PathVariable(value = "username") String username,
                               @PathVariable (value = "email") String email){
        return this.userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or Email ", username + email));

    }
}
