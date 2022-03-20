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
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Validator;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceTest {

    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private  AuthoritiesRepository authoritiesRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private Validator validator;

    @InjectMocks
    private AuthService authService;

    @Test
    public void registeringUser() throws Exception {
        when(modelMapper.map(Mockito.any(), Mockito.eq(Users.class))).thenReturn(new Users());
        when(modelMapper.map(Mockito.any(),  Mockito.eq(UserDetails.class))).thenReturn(new UserDetails());
        when(userDetailsRepository.save(Mockito.any(UserDetails.class))).thenReturn(getUserDetails());
        when(authoritiesRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(new Authorities()));
        assertNotNull(authService.registerUser(getUserRegistration()));
    }

    private UserDetails getUserDetails() {
        Users user = Users.builder()
                .email("email@gmail.com")
                .password("password")
                .id(1L)
                .build();

        return UserDetails.builder()
                .users(user)
                .firstName("Jane")
                .lastName("Doe")
                .gender(Constants.MALE)
                .mobileNo("09453908574")
                .build();
    }

    private UserRegistration getUserRegistration() {
        return UserRegistration.builder()
                .email("email@gmail.com")
                .firstName("Jane")
                .lastName("Doe")
                .password("password")
                .confirmPassword("password")
                .hospitalCode(null)
                .gender(Constants.MALE)
                .mobileNo("09453908574")
                .build();
    }
}
