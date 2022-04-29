package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.Authorities;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.Users;
import io.muffin.inventoryservice.model.dto.UserProfileRequest;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    private @Mock UserDetailsRepository userDetailsRepository;
    private @Mock ObjectMapper objectMapper;
    private @Mock ModelMapper modelMapper;
    private @Mock AuthUtil authUtil;
    private @Mock FileService fileService;
    private @InjectMocks UserService userService;

    private static final String NEW_FILE_DIR = System.getProperty("user.dir");

    @Test
    public void testFindUserById() {
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(getUserDetails()));
        assertNotNull(userService.findUserById("1"));
    }

    @Test
    public void testUpdateUserProfile() throws IOException {
        when(userDetailsRepository.findByUsersEmail(Mockito.any())).thenReturn(getUserDetails());
        when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(UserProfileRequest.class))).thenReturn(new UserProfileRequest());
        when(modelMapper.map(Mockito.any(), Mockito.eq(Users.class))).thenReturn(new Users());
        when(userDetailsRepository.save(Mockito.any(UserDetails.class))).thenReturn(new UserDetails());

        String filePath = String.format("%s%s", NEW_FILE_DIR, "\\input.txt");
        File inputFile = new File(filePath);
        inputFile.createNewFile();
        MultipartFile multipartFile = new MockMultipartFile("input.txt", new FileInputStream(inputFile));

        assertNotNull(userService.updateUserProfile("", multipartFile));
        inputFile.delete();
    }

    private UserDetails getUserDetails() {
        UserDetails userDetails = new UserDetails();
        Users user = new Users();
        user.setId(1L);
        userDetails.setUsers(user);
        return userDetails;
    }
}
