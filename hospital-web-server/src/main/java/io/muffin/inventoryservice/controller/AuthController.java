package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.muffin.inventoryservice.exception.AuthenticationException;
import io.muffin.inventoryservice.jwt.JwtTokenUtil;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.dto.AuthRequestDTO;
import io.muffin.inventoryservice.model.dto.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/authenticate")
public class AuthController {

    private final JwtTokenUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @PostMapping
    public ResponseEntity<JwtTokenResponse> authenticateUser(@RequestBody AuthRequestDTO authRequest) throws JsonProcessingException {

        authenticate(authRequest.getUsername(), authRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAuthToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        JwtUserDetails user = (JwtUserDetails) userDetailsService.loadUserByUsername(username);

        if (jwtUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("INVALID_CREDENTIALS", e);
        }
    }
}
