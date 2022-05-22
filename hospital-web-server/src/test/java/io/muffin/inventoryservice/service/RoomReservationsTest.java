package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.HospitalRoom;
import io.muffin.inventoryservice.model.RoomReservations;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.HospitalRoomResponse;
import io.muffin.inventoryservice.model.dto.ReservationRequest;
import io.muffin.inventoryservice.repository.HospitalRoomRepository;
import io.muffin.inventoryservice.repository.RoomReservationsRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoomReservationsTest {

    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private RoomReservationsRepository roomReservationsRepository;
    @Mock
    private HospitalRoomRepository hospitalRoomRepository;
    @Mock
    private AuthUtil authUtil;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RoomReservationsService roomReservationsService;

    @Test
    public void testFindById() {
        when(roomReservationsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.getRoomReservations()));
        when(modelMapper.map(Mockito.any(HospitalRoom.class), Mockito.eq(HospitalRoomResponse.class))).thenReturn(this.getHospitalRoomResponse());
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        assertNotNull(roomReservationsService.findById("1"));
    }

    @Test
    public void testFindAll() {
        when(roomReservationsRepository
                .findAllRoomReservations(Mockito.anyString(), Mockito.eq("roomName"), Mockito.anyInt(), Mockito.eq(Pageable.ofSize(1))))
                .thenReturn(new PageImpl(new ArrayList()));
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        when(modelMapper.map(Mockito.any(HospitalRoom.class), Mockito.eq(HospitalRoomResponse.class))).thenReturn(this.getHospitalRoomResponse());
        assertNotNull(roomReservationsService.findAll("roomCode", "roomName", "1", Pageable.ofSize(1)));
    }

    @Test
    public void testCreateRoomReservation() throws JsonProcessingException {
        when(roomReservationsRepository.save(Mockito.any(RoomReservations.class))).thenReturn(this.getRoomReservations());
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(hospitalRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.getHospitalRoom()));
        assertNotNull(roomReservationsService.createRoomReservation(this.getReservationRequest()));
    }

    @Test
    public void testUpdateRoomReservation() throws JsonProcessingException {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(roomReservationsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.getRoomReservations()));
        when(roomReservationsRepository.save(Mockito.any(RoomReservations.class))).thenReturn(this.getRoomReservations());
        assertNotNull(roomReservationsService.updateRoomReservation(this.getReservationRequest()));
    }

    @Test
    public void testUpdateRoomReservationStatus() throws JsonProcessingException {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(roomReservationsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.getRoomReservations()));
        when(roomReservationsRepository.save(Mockito.any(RoomReservations.class))).thenReturn(this.getRoomReservations());
        assertNotNull(roomReservationsService.updateRoomReservationStatus("1", 1));
    }

    @Test
    public void testDeleteRoomReservation() {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(roomReservationsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.getRoomReservations()));
        when(roomReservationsRepository.save(Mockito.any(RoomReservations.class))).thenReturn(this.getRoomReservations());
        assertNotNull(roomReservationsService.deleteRoomReservation("1"));
    }

    private RoomReservations getRoomReservations() {
        RoomReservations roomReservations = new RoomReservations();
        roomReservations.setId(1L);
        roomReservations.setHospitalRoom(this.getHospitalRoom());
        roomReservations.setReservedByUserId(1L);
        roomReservations.setUpdatedBy(1L);
        roomReservations.setHasAssociatedAppointmentId(true);
        roomReservations.setAssociatedAppointmentId(1L);
        roomReservations.setReservationStatus(Constants.RESERVATION_CREATED);
        roomReservations.setStartDate(LocalDateTime.now());
        roomReservations.setEndDate(LocalDateTime.now());
        return roomReservations;
    }

    private HospitalRoom getHospitalRoom() {
        return new HospitalRoom();
    }

    private HospitalRoomResponse getHospitalRoomResponse() {
        return new HospitalRoomResponse();
    }

    private ReservationRequest getReservationRequest() {
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setId("1");
        reservationRequest.setHospitalRoomId("1");
        reservationRequest.setHasAssociatedAppointmentId(false);
        reservationRequest.setAssociatedAppointmentId("1");
        reservationRequest.setStartDate(LocalDateTime.now());
        reservationRequest.setEndDate(LocalDateTime.now());
        return reservationRequest;
    }

    private UserDetails getUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1L);
        userDetails.setFirstName("");
        userDetails.setLastName("");
        return userDetails;
    }

    private JwtUserDetails getJwtUserDetails() {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(1L, "", "", "", "ROLE");
        return jwtUserDetails;
    }

}