package com.muffin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffin.model.Authorities;
import com.muffin.model.DoctorCode;
import com.muffin.model.UserDetails;
import com.muffin.model.Users;
import com.muffin.model.dto.GenericResponse;
import com.muffin.model.dto.UserDetailsProfileResponse;
import com.muffin.model.dto.UserRegistration;
import com.muffin.repository.AuthoritiesRepository;
import com.muffin.repository.DoctorCodeRepository;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.repository.UserRepository;
import com.muffin.utility.AuthUtil;
import com.muffin.utility.Constants;
import com.muffin.utility.GlobalFieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final DoctorCodeRepository doctorCodeRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder encoder;
    private final GlobalFieldValidator validator;
    private final AuthUtil authUtil;

    public Long registerUser(UserRegistration userRegistration) throws JsonProcessingException, ConstraintViolationException {
        Users user = modelMapper.map(userRegistration, Users.class);
        UserDetails userDetails = modelMapper.map(userRegistration, UserDetails.class);
        log.info("mappings, USER => [{}]\n USER_DETAILS=> [{}]", objectMapper.writeValueAsString(user), objectMapper.writeValueAsString(userDetails));

        validator.validate(userRegistration);

        user.setId(-1L);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setConfirmed(true);
        user.setCreated(ZonedDateTime.now());
        user.setModified(ZonedDateTime.now());
        user.setEnabled(true);
        user.setDeleted(false);
        userDetails.setId(-1L);
        userDetails.setCreated(ZonedDateTime.now());
        userDetails.setModified(ZonedDateTime.now());
        userDetails.setDeleted(false);

        // if there is a hospital code then it's a doctor
        String doctorCode = userRegistration.getHospitalCode();
        String userAuthority = Constants.AUTHORITY_PATIENT;
        if (StringUtils.hasText(doctorCode)) {
            user.setUserType(Constants.USER_DOCTOR);
            userDetails.setDoctorCodeId(doctorCode.trim());
            userAuthority = Constants.AUTHORITY_DOCTOR;
        } else {
            user.setUserType(Constants.USER_PATIENT);
        }

        log.info("USER_TYPE => [{}]", userAuthority);

        Authorities authority = authoritiesRepository.findByName(userAuthority)
                .orElseThrow(() -> new RuntimeException("Invalid authority"));

        user.setAuthorities(authority);
        userDetails.setUsers(user);
        UserDetails savedUser = userDetailsRepository.save(userDetails);

        return savedUser.getUsers().getId();
    }

    public ResponseEntity<Object> isEmailValid(String emailToValidate) {
        Users user = userRepository.findByEmail(emailToValidate).orElse(null);

        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse(HttpStatus.BAD_REQUEST.value(), "Email is already in use", null));
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> isDoctorCodeValid(String doctorCodeToValidate) {
        DoctorCode doctorCode = doctorCodeRepository.findByDoctorCode(doctorCodeToValidate).orElse(null);

        if (doctorCode != null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericResponse(HttpStatus.BAD_REQUEST.value(), "Doctor code is not valid!", null));
    }

    public ResponseEntity<Object> getLoggedInUser() {
        String email = authUtil.getLoggedUserEmail();
        Users user = userRepository.findByEmail(email).orElse(null);

        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GenericResponse(HttpStatus.UNAUTHORIZED.value(), "User not found!", null));
        }

        UserDetails userDetails = userDetailsRepository.findByUsersId(user.getId()).orElse(null);
        String doctorCode = userDetails.getDoctorCodeId();

        UserDetailsProfileResponse userProfileResponse = modelMapper.map(userDetails, UserDetailsProfileResponse.class);
        userProfileResponse.setId(userDetails.getId());
        userProfileResponse.getUsers().setId(userDetails.getUsers().getId());

        log.info("doctorCode {}", doctorCode);
        if(!Objects.isNull(doctorCode) && StringUtils.hasText(doctorCode)) {
            DoctorCode code = doctorCodeRepository.findByDoctorCode(doctorCode.trim()).orElse(null);
            userProfileResponse.setSpecialization(code.getSpecialization());
        }

        return ResponseEntity.ok(userProfileResponse);
    }
}
