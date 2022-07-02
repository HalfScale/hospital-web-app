package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.muffin.inventoryservice.exception.AuthenticationException;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.*;
import io.muffin.inventoryservice.model.dto.AppointmentRequest;
import io.muffin.inventoryservice.repository.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentHistoryRepository appointmentHistoryRepository;
    @Mock
    private AppointmentDetailsRepository appointmentDetailsRepository;
    @Mock
    private AppointmentDetailsHistoryRepository appointmentDetailsHistoryRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private AuthUtil authUtil;
    @Mock
    private NotificationsService notificationsService;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void testCreateAppointment() throws JsonProcessingException {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        when(userDetailsRepository.save(Mockito.any(UserDetails.class))).thenReturn(this.getUserDetails());
        when(appointmentRepository.save(Mockito.any(Appointment.class))).thenReturn(this.getAppointment());
        when(appointmentHistoryRepository.save(Mockito.any(AppointmentHistory.class))).thenReturn(getAppointmentHistory());
        when(appointmentDetailsRepository.save(Mockito.any(AppointmentDetails.class))).thenReturn(this.getAppointmentDetails());
        when(appointmentDetailsHistoryRepository.save(Mockito.any(AppointmentDetailsHistory.class))).thenReturn(this.getAppointmentDetailsHistory());
        assertEquals(appointmentService.createAppointment(this.getAppointmentRequest()), ResponseEntity.ok(1L));
    }

    @Test
    public void testEditAppointment() throws JsonProcessingException {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(this.getUserDetails()));
        when(appointmentDetailsRepository.findByAppointmentId(Mockito.anyLong())).thenReturn(Optional.of(this.getAppointmentDetails()));
        when(appointmentRepository.save(Mockito.any(Appointment.class))).thenReturn(this.getAppointment());
        when(appointmentHistoryRepository.save(Mockito.any(AppointmentHistory.class))).thenReturn(getAppointmentHistory());
        when(appointmentDetailsRepository.save(Mockito.any(AppointmentDetails.class))).thenReturn(this.getAppointmentDetails());
        when(appointmentDetailsHistoryRepository.save(Mockito.any(AppointmentDetailsHistory.class))).thenReturn(this.getAppointmentDetailsHistory());
        assertEquals(appointmentService.editAppointment("1", this.getAppointmentRequest()), ResponseEntity.ok(1L));
    }

    @Test
    public void testEditAppointmentStatus_DoctorValidAction() throws JsonProcessingException {
        this.setupTestEditAppointmentStatus(Constants.USER_DOCTOR);
        assertEquals(appointmentService.editAppointmentStatus("1", Constants.APPOINTMENT_APPROVED, new HashMap<>()), ResponseEntity.ok(1L));
    }

    @Test
    public void testEditAppointmentStatus_DoctorInvalidAction() {
        this.setupTestEditAppointmentStatus(Constants.USER_DOCTOR);
        assertThrows(AuthenticationException.class, () -> appointmentService.editAppointmentStatus("1", Constants.APPOINTMENT_CANCELLED, new HashMap<>()));
    }

    @Test
    public void testEditAppointmentStatus_PatientValidAction() {
        this.setupTestEditAppointmentStatus(Constants.USER_PATIENT);
        assertEquals(appointmentService.editAppointmentStatus("1", Constants.APPOINTMENT_CANCELLED, new HashMap<>()), ResponseEntity.ok(1L));
    }

    @Test
    public void testEditAppointmentStatus_PatientInvalidAction() {
        this.setupTestEditAppointmentStatus(Constants.USER_PATIENT);
        assertThrows(AuthenticationException.class, () -> appointmentService.editAppointmentStatus("1", Constants.APPOINTMENT_APPROVED, new HashMap<>()));
    }

    @Test
    public void testFindDoctorAppointments() {
        when(appointmentDetailsRepository
                .findDoctorAppointments(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.eq(1L), Mockito.eq(Pageable.ofSize(1))))
                .thenReturn(new PageImpl(new ArrayList()));

        assertNotNull(appointmentService.findDoctorAppointments("2022-08-08 10:00:00", "2022-08-08 12:00:00",
                "1", Pageable.ofSize(1)));
    }

    @Test
    public void testfindAll() {
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(authUtil.getLoggedUserRole()).thenReturn("");
        when(appointmentDetailsRepository.findAllByAppointmentIdAndName(Mockito.anyLong(), Mockito.eq(1L), Mockito.anyString(), Mockito.eq(Pageable.ofSize(1))))
                .thenReturn(new PageImpl(new ArrayList()));
        when(appointmentDetailsRepository.findAllByAppointmentId(Mockito.anyLong(), Mockito.eq(1L), Mockito.eq(Pageable.ofSize(1))))
                .thenReturn(new PageImpl(new ArrayList()));
        when(appointmentDetailsRepository.findAllByName(Mockito.anyLong(), Mockito.eq(""), Mockito.eq(Pageable.ofSize(1))))
                .thenReturn(new PageImpl(new ArrayList()));
        when(appointmentDetailsRepository.findAllByCurrentUser(Mockito.anyLong(), Mockito.eq(Pageable.ofSize(1))))
                .thenReturn(new PageImpl(new ArrayList()));
        assertNotNull(appointmentService.findAll("1", "", Pageable.ofSize(1)));
    }

    private UserDetails setupUser(int userType) {
        Users users = new Users();
        users.setEmail("janedoe@gmail.com");
        users.setUserType(userType);

        UserDetails userDetails = new UserDetails();
        userDetails.setUsers(users);
        userDetails.setFirstName("Jane");
        userDetails.setLastName("Doe");
        userDetails.setAddress("Manila City");
        userDetails.setGender(Constants.FEMALE);
        userDetails.setMobileNo("");

        return userDetails;
    }

    private void setupTestEditAppointmentStatus(int userType) {
        UserDetails userDetails = this.setupUser(userType);
        when(authUtil.getCurrentUser()).thenReturn(this.getJwtUserDetails());
        when(appointmentDetailsRepository.findByAppointmentId(Mockito.anyLong())).thenReturn(Optional.of(this.getAppointmentDetails()));
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(userDetails));
        when(appointmentRepository.save(Mockito.any(Appointment.class))).thenReturn(this.getAppointment());
        when(appointmentHistoryRepository.save(Mockito.any(AppointmentHistory.class))).thenReturn(this.getAppointmentHistory());
        when(appointmentDetailsRepository.save(Mockito.any(AppointmentDetails.class))).thenReturn(this.getAppointmentDetails());
        when(appointmentDetailsHistoryRepository.save(Mockito.any(AppointmentDetailsHistory.class))).thenReturn(this.getAppointmentDetailsHistory());
    }
    private AppointmentRequest getAppointmentRequest() {
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setId(1L);
        appointmentRequest.setDoctorId(1L);
        appointmentRequest.setFirstTime(true);
        appointmentRequest.setStartDate(LocalDateTime.now());
        appointmentRequest.setEndDate(LocalDateTime.now());
        appointmentRequest.setReasonForAppointment("");
        return appointmentRequest;
    }

    private Appointment getAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(this.getUserDetails());
        appointment.setDoctor(this.getUserDetails());
        return appointment;
    }

    private AppointmentDetails getAppointmentDetails() {
        AppointmentDetails appointmentDetails = new AppointmentDetails();
        appointmentDetails.setAppointment(this.getAppointment());
        appointmentDetails.setAppointmentReason("Reason for appointment");
        return appointmentDetails;
    }

    private AppointmentHistory getAppointmentHistory() {
        AppointmentHistory appointmentHistory = new AppointmentHistory();
        return appointmentHistory;
    }

    private AppointmentDetailsHistory getAppointmentDetailsHistory() {
        AppointmentDetailsHistory appointmentDetailsHistory = new AppointmentDetailsHistory();
        return appointmentDetailsHistory;
    }

    private JwtUserDetails getJwtUserDetails() {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(1L, "email", "password",
                "name", "role");
        return jwtUserDetails;
    }

    private UserDetails getUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsers(this.getUsers());
        userDetails.setFirstName("Jane");
        userDetails.setLastName("Doe");
        userDetails.setAddress("Manila City");
        userDetails.setGender(Constants.FEMALE);
        userDetails.setMobileNo("");
        return userDetails;
    }

    private Users getUsers() {
        Users users = new Users();
        users.setEmail("janedoe@gmail.com");
        users.setUserType(Constants.USER_PATIENT);
        return users;
    }

}