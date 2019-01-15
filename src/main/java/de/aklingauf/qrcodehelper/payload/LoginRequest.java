package de.aklingauf.qrcodehelper.payload;

import javax.validation.constraints.NotBlank;

// From https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-2/

public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
