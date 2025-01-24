package com.muffin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffin.jwt.JwtUserDetails;
import com.muffin.model.HospitalRooms;
import com.muffin.model.RoomReservations;
import com.muffin.model.UserDetails;
import com.muffin.model.dto.HospitalRoomRequest;
import com.muffin.model.dto.HospitalRoomResponse;
import com.muffin.repository.HospitalRoomRepository;
import com.muffin.repository.RoomReservationsRepository;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.utility.AuthUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HospitalRoomsTest {

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
    private RoomReservationsRepository roomReservationsRepository;

    @InjectMocks
    private HospitalRoomService hospitalRoomService;

    private static final String NEW_FILE_DIR = System.getProperty("user.dir");

    @Test
    public void testFindById() {
        when(hospitalRoomRepository.findByIdAndDeletedFalse(Mockito.anyLong())).thenReturn(Optional.of(this.getHospitalRoom()));
        when(modelMapper.map(Mockito.any(HospitalRooms.class), Mockito.eq(HospitalRoomResponse.class))).thenReturn(this.getHospitalRoomResponse());
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
        when(roomReservationsRepository.findAllByHospitalRoomId(Mockito.anyLong())).thenReturn(this.getRoomReservations());
        when(roomReservationsRepository.save(Mockito.any(RoomReservations.class))).thenReturn(this.getRoomReservation());
        when(hospitalRoomRepository.save(Mockito.any(HospitalRooms.class))).thenReturn(this.getHospitalRoom());

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
        when(hospitalRoomRepository.save(Mockito.any(HospitalRooms.class))).thenReturn(this.getHospitalRoom());

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
        when(hospitalRoomRepository.save(Mockito.any(HospitalRooms.class))).thenReturn(this.getHospitalRoom());
        assertNotNull(hospitalRoomService.deleteHospitalRoom("1"));
    }

    private HospitalRooms getHospitalRoom() {
        HospitalRooms hospitalRooms = new HospitalRooms();
        hospitalRooms.setId(1L);
        hospitalRooms.setCreatedBy(1L);
        hospitalRooms.setUpdatedBy(1L);
        return hospitalRooms;
    }

    private RoomReservations getRoomReservation() {
        RoomReservations roomReservations = new RoomReservations();
        roomReservations.setId(1L);
        return roomReservations;
    }

    private List<RoomReservations> getRoomReservations() {
        List<RoomReservations> reservations = Arrays.asList(this.getRoomReservation());
        return reservations;
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