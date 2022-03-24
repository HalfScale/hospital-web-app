package io.muffin.inventoryservice.utility;

import io.muffin.inventoryservice.jwt.JwtUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthUtil {

    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication;
        }

        return null;
    }

    //returns authenticated user's email
    public String getLoggedUserEmail() {
        Authentication authentication = this.getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    public JwtUserDetails getCurrentUser() {

        if(!Objects.isNull(this.getAuthentication())) {
            return (JwtUserDetails) this.getAuthentication().getPrincipal();
        }

        return null;
    }
}
