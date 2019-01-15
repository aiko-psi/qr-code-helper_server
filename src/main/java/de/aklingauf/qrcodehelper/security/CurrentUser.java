package de.aklingauf.qrcodehelper.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

// From https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-2/

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
