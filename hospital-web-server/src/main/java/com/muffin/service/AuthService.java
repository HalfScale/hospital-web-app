package com.muffin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffin.mapper.UserDetailsMapper;
import com.muffin.mapper.UsersMapper;
import com.muffin.model.DoctorCode;
import com.muffin.model.UserDetails;
import com.muffin.model.Users;
import com.muffin.model.dto.GenericResponse;
import com.muffin.model.dto.UserDetailsProfileResponse;
import com.muffin.model.dto.UserRegistration;
import com.muffin.repository.DoctorCodeRepository;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.repository.UserRepository;
import com.muffin.utility.AuthUtil;
import com.muffin.utility.GlobalFieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final DoctorCodeRepository doctorCodeRepository;
    private final ModelMapper modelMapper;
    private final UsersMapper usersMapper;
    private final UserDetailsMapper userDetailsMapper;
    private final ObjectMapper objectMapper;
    private final GlobalFieldValidator validator;
    private final AuthUtil authUtil;

    public UserDetails registerUser(UserRegistration userRegistration) throws JsonProcessingException, ConstraintViolationException {
        validator.validate(userRegistration);

        Users user = usersMapper.mapToUsers(userRegistration);
        UserDetails userDetails = userDetailsMapper.mapToUserDetails(userRegistration);

        log.info("mappings, USER => [{}]\n USER_DETAILS=> [{}]", objectMapper.writeValueAsString(user), objectMapper.writeValueAsString(userDetails));

        userDetails.setUsers(user);

        return userDetailsRepository.save(userDetails);
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
        DoctorCode doctorCode = doctorCodeRepository.findTopByCodeOrderByCreatedDesc(doctorCodeToValidate).orElse(null);

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
            DoctorCode code = doctorCodeRepository.findTopByCodeOrderByCreatedDesc(doctorCode.trim()).orElse(null);
            userProfileResponse.setSpecialization(code.getSpecialization());
        }

        return ResponseEntity.ok(userProfileResponse);
    }
}
