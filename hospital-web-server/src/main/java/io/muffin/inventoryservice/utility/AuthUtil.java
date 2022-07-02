package io.muffin.inventoryservice.utility;

import io.muffin.inventoryservice.jwt.JwtUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

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

    public String getLoggedUserRole() {
        JwtUserDetails currentJwtUserDetails = this.getCurrentUser();
        Long currentUserId = currentJwtUserDetails.getId();
        Optional<? extends GrantedAuthority> authorities = currentJwtUserDetails.getAuthorities().stream().findFirst();
        return authorities.get().getAuthority();
    }

    public JwtUserDetails getCurrentUser() {

        if(!Objects.isNull(this.getAuthentication())) {
            return (JwtUserDetails) this.getAuthentication().getPrincipal();
        }

        return null;
    }
}
