package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.Authorities;
import io.muffin.inventoryservice.model.DoctorCode;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.Users;
import io.muffin.inventoryservice.model.dto.GenericResponse;
import io.muffin.inventoryservice.model.dto.UserDetailsProfileResponse;
import io.muffin.inventoryservice.model.dto.UserRegistration;
import io.muffin.inventoryservice.repository.AuthoritiesRepository;
import io.muffin.inventoryservice.repository.DoctorCodeRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.repository.UserRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import io.muffin.inventoryservice.utility.GlobalFieldValidator;
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
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setEnabled(true);
        user.setDeleted(false);
        userDetails.setId(-1L);
        userDetails.setCreated(LocalDateTime.now());
        userDetails.setModified(LocalDateTime.now());
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
