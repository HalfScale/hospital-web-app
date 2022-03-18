package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.jwt.JwtTokenUtil;
import io.muffin.inventoryservice.model.dto.UserRegistration;
import io.muffin.inventoryservice.service.AuthService;
import io.muffin.inventoryservice.service.CustomUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//@WebMvcTest(AuthController.class)
//@ActiveProfiles(value = "test")
public class AuthControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthService authService;
//
//    @MockBean
//    private JwtTokenUtil jwtTokenUtil;
//
//    @MockBean
//    private UserDetailsService userDetailsService;
//
//    @MockBean
//    private AuthenticationManager authenticationManager;
//
//    @MockBean
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser
//    @DisplayName("Register user")
//    public void registrationShouldReturnIdFromService() throws Exception {
//        when(authService.registerUser(Mockito.any(UserRegistration.class))).thenReturn(Mockito.anyLong());
//
//        this.mockMvc.perform(post("/auth/register")
//                        .content(String.valueOf(Mockito.anyLong()))
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andDo(print()).andExpect(status().isOk());
//    }

}
