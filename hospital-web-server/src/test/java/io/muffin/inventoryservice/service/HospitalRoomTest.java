package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.HospitalRoom;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.HospitalRoomRequest;
import io.muffin.inventoryservice.model.dto.HospitalRoomResponse;
import io.muffin.inventoryservice.repository.HospitalRoomRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HospitalRoomTest {

    @Mock
    private HospitalRoomRepository hospitalRoomRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private AuthUtil authUtil;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private DeprecatedFileService deprecatedFileService;

    @InjectMocks
    private HospitalRoomService hospitalRoomService;

    private static final String NEW_FILE_DIR = System.getProperty("user.dir");

    @Test
    public void testFindById() {
        when(hospitalRoomRepository.findByIdAndDeletedFalse(Mockito.anyLong())).thenReturn(Optional.of(this.getHospitalRoom()));
        when(modelMapper.map(Mockito.any(HospitalRoom.class), Mockito.eq(HospitalRoomResponse.class))).thenReturn(this.getHospitalRoomResponse());
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        assertNotNull(hospitalRoomService.findById("1"));
    }

    @Test
    public void testFindAll() {
        when(hospitalRoomRepository.findAllRoomByCodeOrName(Mockito.anyString(),
                Mockito.eq("name"), Mockito.eq(Pageable.ofSize(1)))).thenReturn(new PageImpl(new ArrayList()));
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        assertNotNull(hospitalRoomService.findAll("code", "name", Pageable.ofSize(1)));
    }

    @Test
    public void testAddHospitalRoom() throws IOException {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(HospitalRoomRequest.class))).thenReturn(this.getHospitalRoomRequest());
        when(hospitalRoomRepository.save(Mockito.any(HospitalRoom.class))).thenReturn(this.getHospitalRoom());

        String filePath = String.format("%s%s", NEW_FILE_DIR, "\\input.txt");
        File inputFile = new File(filePath);
        inputFile.createNewFile();
        MultipartFile multipartFile = new MockMultipartFile("input.txt", new FileInputStream(inputFile));

        assertNotNull(hospitalRoomService.addHospitalRoom(this.getHospitalRoomRequestToString(), multipartFile));

        inputFile.delete();
    }

    @Test
    public void testUpdateHospitalRoom() throws IOException {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(HospitalRoomRequest.class))).thenReturn(this.getHospitalRoomRequest());
        when(hospitalRoomRepository.findByIdAndDeletedFalse(Mockito.any())).thenReturn(Optional.of(this.getHospitalRoom()));
        when(hospitalRoomRepository.save(Mockito.any(HospitalRoom.class))).thenReturn(this.getHospitalRoom());

        String filePath = String.format("%s%s", NEW_FILE_DIR, "\\input.txt");
        File inputFile = new File(filePath);
        inputFile.createNewFile();
        MultipartFile multipartFile = new MockMultipartFile("input.txt", new FileInputStream(inputFile));

        assertNotNull(hospitalRoomService.updateHospitalRoom(this.getHospitalRoomRequestToString(), multipartFile));

        inputFile.delete();
    }

    @Test
    public void testDeleteHospitalRoom() {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(hospitalRoomRepository.findByIdAndDeletedFalse(Mockito.any())).thenReturn(Optional.of(this.getHospitalRoom()));
        when(hospitalRoomRepository.save(Mockito.any(HospitalRoom.class))).thenReturn(this.getHospitalRoom());
        assertNotNull(hospitalRoomService.deleteHospitalRoom("1"));
    }

    private HospitalRoom getHospitalRoom() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setCreatedBy(1L);
        hospitalRoom.setUpdatedBy(1L);
        return hospitalRoom;
    }

    private HospitalRoomRequest getHospitalRoomRequest() {
        HospitalRoomRequest hospitalRoomRequest = new HospitalRoomRequest();
        hospitalRoomRequest.setId(1L);
        hospitalRoomRequest.setRoomCode("");
        hospitalRoomRequest.setRoomName("");
        hospitalRoomRequest.setDescription("");
        return hospitalRoomRequest;
    }

    private HospitalRoomResponse getHospitalRoomResponse() {
        HospitalRoomResponse hospitalRoomResponse = new HospitalRoomResponse();
        return hospitalRoomResponse;
    }

    private String getHospitalRoomRequestToString() {
        return "{\"id\":\"-1\",\"roomCode\":\"101\",\"roomName\":\"Sample Room\",\"description\":\"Sample Description\"}";
    }

    private UserDetails getUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName("");
        userDetails.setLastName("");
        return userDetails;
    }

    private JwtUserDetails getJwtUserDetails() {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(1L, "", "", "", "ROLE");
        return jwtUserDetails;
    }
}