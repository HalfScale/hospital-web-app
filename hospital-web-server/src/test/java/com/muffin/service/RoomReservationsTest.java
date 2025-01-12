package com.muffin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffin.jwt.JwtUserDetails;
import com.muffin.model.HospitalRoom;
import com.muffin.model.RoomReservations;
import com.muffin.model.UserDetails;
import com.muffin.model.dto.HospitalRoomResponse;
import com.muffin.model.dto.ReservationRequest;
import com.muffin.repository.HospitalRoomRepository;
import com.muffin.repository.RoomReservationsRepository;
import com.muffin.repository.UserDetailsRepository;
import com.muffin.utility.AuthUtil;
import com.muffin.utility.Constants;
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
import java.time.ZonedDateTime;
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
        when(roomReservationsRepository.findByIdNotDeleted(Mockito.anyLong())).thenReturn(Optional.of(this.getRoomReservations()));
        when(modelMapper.map(Mockito.any(HospitalRoom.class), Mockito.eq(HospitalRoomResponse.class))).thenReturn(this.getHospitalRoomResponse());
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        assertNotNull(roomReservationsService.findById("1"));
    }

    @Test
    public void testFindAll() {
        when(roomReservationsRepository
                .findAllRoomReservations(Mockito.anyString(), Mockito.eq("roomName"), Mockito.anyString(), Mockito.eq(Pageable.ofSize(1))))
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
        assertNotNull(roomReservationsService.updateRoomReservationStatus("1", "1"));
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
        roomReservations.setReservationStatus(String.valueOf(Constants.RESERVATION_CREATED));
        roomReservations.setStartDate(ZonedDateTime.now());
        roomReservations.setEndDate(ZonedDateTime.now());
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
        reservationRequest.setStartDate(ZonedDateTime.now());
        reservationRequest.setEndDate(ZonedDateTime.now());
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