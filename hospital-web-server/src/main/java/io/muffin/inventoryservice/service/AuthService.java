package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.Authorities;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.Users;
import io.muffin.inventoryservice.model.dto.UserRegistration;
import io.muffin.inventoryservice.repository.AuthoritiesRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsRepository userDetailsRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder encoder;

    public UserRegistration testMethod(UserRegistration args) throws JsonProcessingException {
        log.info("args {}",args);
        log.info("modelMapper is null? {}, {}", Objects.isNull(modelMapper), modelMapper.map(args, Users.class));
        Users user = modelMapper.map(args, Users.class);
        UserDetails userDetails = modelMapper.map(args, UserDetails.class);
        log.info("user {}", user);
        log.info("userDetails {}", userDetails);
        return args;
    }

    public Long registerUser(UserRegistration userRegistration) throws JsonProcessingException {
        log.info("userRegistration => [{}]", objectMapper.writeValueAsString(userRegistration));
        Users user = modelMapper.map(userRegistration, Users.class);
        UserDetails userDetails = modelMapper.map(userRegistration, UserDetails.class);
        log.info("mappings, USER => [{}]\n USER_DETAILS=> [{}]", objectMapper.writeValueAsString(user), objectMapper.writeValueAsString(userDetails));

        user.setId(-1L);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setConfirmed(true);
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setEnabled(true);
        user.setDeleted(false);
        log.info("Setters");
        userDetails.setId(-1L);
        userDetails.setCreated(LocalDateTime.now());
        userDetails.setModified(LocalDateTime.now());
        userDetails.setDeleted(false);

        // if there is a hospital code then it's a doctor
        String doctorCode = userRegistration.getHospitalCode();
        String userAuthority = Constants.AUTHORITY_PATIENT;
        if(Objects.isNull(doctorCode) || !doctorCode.isEmpty()) {
            user.setUserType(Constants.USER_DOCTOR);
            userAuthority = Constants.AUTHORITY_DOCTOR;
        }else {
            user.setUserType(Constants.USER_PATIENT);
        }

        log.info("USER_TYPE => [{}]", userAuthority);

        Authorities authority = authoritiesRepository.findByName(userAuthority)
                .orElseThrow(() -> new RuntimeException("Invalid authority"));

        user.setAuthorities(authority);
        userDetails.setUsers(user);
        log.info("SAVE => {}", objectMapper.writeValueAsString(userDetails));
        UserDetails savedUser = userDetailsRepository.save(userDetails);

        return savedUser.getUsers().getId();
    }
}
