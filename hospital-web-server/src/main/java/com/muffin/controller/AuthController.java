package com.muffin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffin.exception.AuthenticationException;
import com.muffin.jwt.JwtTokenUtil;
import com.muffin.jwt.JwtUserDetails;
import com.muffin.model.dto.*;
import com.muffin.service.AuthService;
import com.muffin.utility.GlobalFieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.muffin.utility.Constants.API_VERSION_V1;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(API_VERSION_V1 + "/auth")
public class AuthController {

    private final JwtTokenUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final GlobalFieldValidator validator;

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistration userRegistration) throws JsonProcessingException {
        log.info("REGISTER_USER => [{}]", objectMapper.writeValueAsString(userRegistration));
        return ResponseEntity.ok(authService.registerUser(userRegistration));
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenResponse> authenticateUser(@RequestBody AuthRequest authRequest) throws JsonProcessingException {
        log.info("AUTHENTICATE_USER_REQUEST => [{}]", objectMapper.writeValueAsString(authRequest));
        validator.validate(authRequest);
        authenticate(authRequest.getEmail(), authRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @PostMapping(path = "/validate/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> isEmailAvailable(@RequestBody EmailValidationRequest emailToValidate) {
        log.info("CHECK_IF_EMAIL_IS_VALID => [{}]", emailToValidate.getEmail());
        validator.validate(emailToValidate);
        return authService.isEmailValid(emailToValidate.getEmail());
    }

    @PostMapping(path = "/validate/doctorCode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> isDoctorCodeValid(@RequestBody DoctorCodeValidationRequest doctorCodeValidationRequest) {
        log.info("CHECK_IF_DOCTOR_CODE_IS_VALID => [{}]", doctorCodeValidationRequest.getDoctorCode());
        validator.validate(doctorCodeValidationRequest);
        return authService.isDoctorCodeValid(doctorCodeValidationRequest.getDoctorCode());
    }

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getLoggedInUser(){
        log.info("GET_LOGGED_USER");
        return authService.getLoggedInUser();
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
