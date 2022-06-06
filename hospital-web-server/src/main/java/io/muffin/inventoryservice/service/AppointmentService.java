package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.Appointment;
import io.muffin.inventoryservice.model.AppointmentDetails;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.AppointmentRequest;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.muffin.inventoryservice.repository.AppointmentRepository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final AuthUtil authUtil;

    public ResponseEntity<Object> findById(String id) {
        return null;
    }

    public ResponseEntity<Object> findAll() {
        return null;
    }

    public ResponseEntity<Object> createAppointment(AppointmentRequest appointmentRequest) {
        JwtUserDetails currentUser = authUtil.getCurrentUser();

        UserDetails patient = userDetailsRepository.findByUsersId(currentUser.getId())
                .orElseThrow(() -> new HospitalException("Patient not found!"));

        UserDetails doctor = userDetailsRepository.findByUsersId(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new HospitalException("Doctor not found!"));

        Appointment appointment = new Appointment();
        AppointmentDetails appointmentDetails = new AppointmentDetails();
        appointment.setId(appointmentRequest.getId());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentStatus(Constants.APPOINTMENT_PENDING);
        appointment.setCreated(LocalDateTime.now());
        appointment.setModified(LocalDateTime.now());
        appointment.setCreated(LocalDateTime.now());

        if(StringUtils.hasText(appointmentRequest.getAddress())) {
            patient.setAddress(appointmentRequest.getAddress());
            userDetailsRepository.save(patient);
        }

        appointmentRepository.save(appointment);

        appointmentDetails.setAppointment(appointment);
        appointmentDetails.setStartDate(appointmentRequest.getStartDate());
        appointmentDetails.setEndDate(appointmentRequest.getEndDate());
        appointmentDetails.setFirstTime(appointmentRequest.isFirstTime());

        return ResponseEntity.ok(appointment.getId());
    }

    public ResponseEntity<Object> editAppointment(AppointmentRequest appointmentRequest) {
        JwtUserDetails currentUser = authUtil.getCurrentUser();

        Appointment appointment = appointmentRepository.findById(appointmentRequest.getId())
                .orElseThrow(() -> new HospitalException("Appointment not found!"));

        appointment.setModified(LocalDateTime.now());

        appointmentRepository.save(appointment);

        return ResponseEntity.ok(appointment.getId());
    }

}