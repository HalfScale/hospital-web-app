package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.Authorities;
import io.muffin.inventoryservice.model.DoctorCode;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.Users;
import io.muffin.inventoryservice.model.dto.UserDetailsProfileResponse;
import io.muffin.inventoryservice.model.dto.UserProfileResponse;
import io.muffin.inventoryservice.model.dto.UserRegistration;
import io.muffin.inventoryservice.repository.AuthoritiesRepository;
import io.muffin.inventoryservice.repository.DoctorCodeRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.repository.UserRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import io.muffin.inventoryservice.utility.GlobalFieldValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Validator;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceTest {

    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private AuthoritiesRepository authoritiesRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private GlobalFieldValidator validator;
    @Mock
    private DoctorCodeRepository doctorCodeRepository;
    @Mock
    private AuthUtil authUtil;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegisteringUser() throws Exception {
        when(modelMapper.map(Mockito.any(), Mockito.eq(Users.class))).thenReturn(new Users());
        when(modelMapper.map(Mockito.any(), Mockito.eq(UserDetails.class))).thenReturn(new UserDetails());
        when(userDetailsRepository.save(Mockito.any(UserDetails.class))).thenReturn(getUserDetails());
        when(authoritiesRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(new Authorities()));
        assertNotNull(authService.registerUser(this.getUserRegistration()));
    }

    @Test
    public void testIsEmailValid() {
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(new Users()));
        assertNotNull(authService.isEmailValid(""));
    }

    @Test
    public void testIsEmailValid_InvalidEmail() {
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        assertNotNull(authService.isEmailValid(""));
    }

    @Test
    public void testIsDoctorCodeValid() {
        when(doctorCodeRepository.findByDoctorCode(Mockito.anyString())).thenReturn(Optional.of(new DoctorCode()));
        assertNotNull(authService.isDoctorCodeValid(""));
    }

    @Test
    public void testIsDoctorCodeValid_InvalidCode() {
        when(doctorCodeRepository.findByDoctorCode(Mockito.anyString())).thenReturn(Optional.empty());
        assertNotNull(authService.isDoctorCodeValid(""));
    }

    @Test
    public void testGetLoggedInUser() {
        when(authUtil.getLoggedUserEmail()).thenReturn("");
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(new Users()));
        when(userDetailsRepository.findByUsersId(Mockito.any())).thenReturn(Optional.of(getUserDetails()));
        when(modelMapper.map(Mockito.any(), Mockito.eq(UserDetailsProfileResponse.class))).thenReturn(getUserDetailsProfileResponse());
        when(doctorCodeRepository.findByDoctorCode(Mockito.any())).thenReturn(Optional.of(new DoctorCode()));
        assertNotNull(authService.getLoggedInUser());
    }

    private UserDetailsProfileResponse getUserDetailsProfileResponse() {

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setId(1L);
        UserDetailsProfileResponse userDetailsProfileResponse = new UserDetailsProfileResponse();
        userDetailsProfileResponse.setUsers(userProfileResponse);
        return userDetailsProfileResponse;
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
                .doctorCodeId("001M3")
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
