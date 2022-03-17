package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.Users;
import io.muffin.inventoryservice.model.dto.UserRegistration;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsRepository userDetailsRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public Long registerUser(UserRegistration userRegistration) throws JsonProcessingException {
        Users user = modelMapper.map(userRegistration, Users.class);
        UserDetails userDetails = modelMapper.map(userRegistration, UserDetails.class);
        log.info("mappings, USER => [{}], USER_DETAILS=> [{}]", objectMapper.writeValueAsString(user), objectMapper.writeValueAsString(userDetails));
        return 1L;
    }
}
