package com.muffin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffin.mapper.UserDetailsMapper;
import com.muffin.mapper.UsersMapper;
import com.muffin.model.DoctorCode;
import com.muffin.model.UserDetails;
import com.muffin.model.Users;
import com.muffin.model.dto.UserDetailsProfileResponse;
import com.muffin.model.dto.UserProfileResponse;
import com.muffin.model.dto.UserRegistration;
import com.muffin.repository.AuthoritiesRepository;
import com.muffin.repository.DoctorCodeRepository;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.repository.UserRepository;
import com.muffin.utility.AuthUtil;
import com.muffin.utility.Constants;
import com.muffin.utility.GlobalFieldValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Mock
    private UsersMapper usersMapper;
    @Mock
    private UserDetailsMapper userDetailsMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegisteringUser() throws Exception {
        when(usersMapper.mapToUsers(Mockito.any(UserRegistration.class))).thenReturn(new Users());
        when(userDetailsMapper.mapToUserDetails(Mockito.any(UserRegistration.class))).thenReturn(new UserDetails());
        when(userDetailsRepository.save(Mockito.any(UserDetails.class))).thenReturn(getUserDetails());
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
