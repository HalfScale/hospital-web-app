package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.exception.AuthenticationException;
import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.*;
import io.muffin.inventoryservice.model.dto.AppointmentRequest;
import io.muffin.inventoryservice.repository.*;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentHistoryRepository appointmentHistoryRepository;
    private final AppointmentDetailsRepository appointmentDetailsRepository;
    private final AppointmentDetailsHistoryRepository appointmentDetailsHistoryRepository;
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

        if (patient.getUsers().getUserType() == Constants.USER_DOCTOR) {
            throw new AuthenticationException("Unauthorized user to create appointment");
        }

        UserDetails doctor = userDetailsRepository.findByUsersId(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new HospitalException("Doctor not found!"));

        int appointmentStatus = Constants.APPOINTMENT_PENDING;

        Appointment appointment = new Appointment();
        AppointmentDetails appointmentDetails = new AppointmentDetails();
        new AppointmentDetailsHistory();

        appointment.setId(appointmentRequest.getId());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentStatus(appointmentStatus);
        appointment.setCreated(LocalDateTime.now());
        appointment.setModified(LocalDateTime.now());

        if (StringUtils.hasText(appointmentRequest.getAddress())) {
            patient.setAddress(appointmentRequest.getAddress());
            userDetailsRepository.save(patient);
        }

        appointmentRepository.save(appointment);

        AppointmentHistory appointmentHistory = this.mapToAppointmentHistory(appointment, patient, doctor);

        appointmentHistoryRepository.save(appointmentHistory);

        appointmentDetails.setAppointment(appointment);
        appointmentDetails.setFirstName(patient.getFirstName());
        appointmentDetails.setLastName(patient.getLastName());
        appointmentDetails.setAddress(patient.getAddress());
        appointmentDetails.setGender(patient.getGender());
        appointmentDetails.setFirstTime(appointmentRequest.isFirstTime());
        appointmentDetails.setStartDate(appointmentRequest.getStartDate());
        appointmentDetails.setEndDate(appointmentRequest.getEndDate());
        appointmentDetails.setMobileNo(patient.getMobileNo());
        appointmentDetails.setEmail(patient.getUsers().getEmail());
        appointmentDetails.setAppointmentReason(appointmentRequest.getReasonForAppointment());
        appointmentDetails.setCreated(LocalDateTime.now());
        appointmentDetails.setModified(LocalDateTime.now());

        appointmentDetailsRepository.save(appointmentDetails);

        AppointmentDetailsHistory appointmentDetailsHistory = this.mapToAppointmentDetailsHistory(appointment, appointmentDetails, appointmentRequest, patient);

        appointmentDetailsHistoryRepository.save(appointmentDetailsHistory);

        return ResponseEntity.ok(appointment.getId());
    }

    public ResponseEntity<Object> editAppointment(String appointmentId, AppointmentRequest appointmentRequest) {
        JwtUserDetails currentUser = authUtil.getCurrentUser();

        UserDetails userToValidate = userDetailsRepository.findByUsersId(currentUser.getId())
                .orElseThrow(() -> new HospitalException("User not found!"));

        if (userToValidate.getUsers().getUserType() == Constants.USER_DOCTOR) {
            throw new AuthenticationException("Unauthorized user to update appointment");
        }

        AppointmentDetails appointmentDetails = appointmentDetailsRepository.findByAppointmentId(Long.valueOf(appointmentId))
                .orElseThrow(() -> new HospitalException("Appointment is not existing!"));

        Appointment appointment = appointmentDetails.getAppointment();
        UserDetails patient = appointment.getPatient();
        UserDetails doctor = appointment.getDoctor();

        appointment.setModified(LocalDateTime.now());

        appointmentRepository.save(appointment);

        AppointmentHistory appointmentHistory = this.mapToAppointmentHistory(appointment, patient, doctor);

        appointmentHistoryRepository.save(appointmentHistory);

        appointmentDetails.setFirstTime(appointmentRequest.isFirstTime());
        appointmentDetails.setStartDate(appointmentRequest.getStartDate());
        appointmentDetails.setEndDate(appointmentRequest.getEndDate());
        appointmentDetails.setAppointmentReason(appointmentRequest.getReasonForAppointment());
        appointmentDetails.setModified(LocalDateTime.now());

        appointmentDetailsRepository.save(appointmentDetails);

        AppointmentDetailsHistory appointmentDetailsHistory = this.mapToAppointmentDetailsHistory(appointment, appointmentDetails, appointmentRequest, patient);

        appointmentDetailsHistoryRepository.save(appointmentDetailsHistory);

        return ResponseEntity.ok(appointment.getId());
    }

    public ResponseEntity<Object> editAppointmentStatus(String appointmentId, int status, Map<String, String> editAppointmentStatusRequest) {
        JwtUserDetails currentUser = authUtil.getCurrentUser();
        this.validateAppointmentStatus(status);

        AppointmentDetails appointmentDetails = appointmentDetailsRepository.findByAppointmentId(Long.valueOf(appointmentId))
                .orElseThrow(() -> new HospitalException("Appointment not existing!"));

        Appointment appointment = appointmentDetails.getAppointment();

        UserDetails userDetails = userDetailsRepository.findByUsersId(currentUser.getId())
                .orElseThrow(() -> new HospitalException("User not found!"));

        int userType = userDetails.getUsers().getUserType();

        if (status == Constants.APPOINTMENT_APPROVED || status == Constants.APPOINTMENT_REJECTED) {

            if (userType != Constants.USER_DOCTOR) {
                throw new AuthenticationException("Unauthorized user to perform action!");
            }

            appointment.setAppointmentStatus(status);
            appointment.setModified(LocalDateTime.now());

            appointmentRepository.save(appointment);

            AppointmentHistory appointmentHistory = this.mapToAppointmentHistory(appointment, appointment.getPatient(), appointment.getDoctor());
            appointmentHistoryRepository.save(appointmentHistory);

            AppointmentDetailsHistory appointmentDetailsHistory = this.mapToAppointmentDetailsHistory(appointment, appointmentDetails, null,
                    appointment.getPatient());

            if (status == Constants.APPOINTMENT_REJECTED) {
                String reason = editAppointmentStatusRequest.get("reason");
                appointmentDetails.setCancelReason(reason);
                appointmentDetailsHistory.setCancelReason(reason);
                appointmentDetails.setModified(LocalDateTime.now());
            }

            appointmentDetailsRepository.save(appointmentDetails);
            appointmentDetailsHistoryRepository.save(appointmentDetailsHistory);


        }

        if (status == Constants.APPOINTMENT_CANCELLED) {

            if (userType != Constants.USER_PATIENT) {
                throw new AuthenticationException("Unauthorized user to perform action!");
            }

            appointment.setAppointmentStatus(status);
            appointment.setModified(LocalDateTime.now());

            appointmentRepository.save(appointment);

            AppointmentHistory appointmentHistory = this.mapToAppointmentHistory(appointment, appointment.getPatient(), appointment.getDoctor());
            appointmentHistoryRepository.save(appointmentHistory);

            appointmentDetails.setModified(LocalDateTime.now());
            appointmentDetails.setCancelReason(editAppointmentStatusRequest.get("reason"));

            appointmentDetailsRepository.save(appointmentDetails);

            AppointmentDetailsHistory appointmentDetailsHistory = this.mapToAppointmentDetailsHistory(appointment, appointmentDetails, null, appointment.getPatient());
            appointmentDetailsHistoryRepository.save(appointmentDetailsHistory);
        }

        return ResponseEntity.ok(appointment.getId());
    }

    private void validateAppointmentStatus(int targetStatus) {
        List<Integer> statuses = Arrays.asList(Constants.APPOINTMENT_APPROVED,
                Constants.APPOINTMENT_REJECTED, Constants.APPOINTMENT_CANCELLED);

        statuses.stream().filter(appointmentStatus -> appointmentStatus == targetStatus)
                .findFirst().orElseThrow(() -> new HospitalException("Invalid status code!"));
    }

    private AppointmentHistory mapToAppointmentHistory(Appointment appointment, UserDetails patient, UserDetails doctor) {
        AppointmentHistory appointmentHistory = new AppointmentHistory();
        appointmentHistory.setAppointment(appointment);
        appointmentHistory.setPatient(patient);
        appointmentHistory.setDoctor(doctor);
        appointmentHistory.setAppointmentStatus(appointment.getAppointmentStatus());
        appointmentHistory.setCreated(LocalDateTime.now());
        appointmentHistory.setModified(LocalDateTime.now());
        return appointmentHistory;
    }

    private AppointmentDetailsHistory mapToAppointmentDetailsHistory(Appointment appointment, AppointmentDetails appointmentDetails,
                                                                     AppointmentRequest appointmentRequest, UserDetails patient) {

        AppointmentDetailsHistory appointmentDetailsHistory = new AppointmentDetailsHistory();

        boolean firstTime = appointmentRequest != null ? appointmentRequest.isFirstTime() : appointmentDetails.isFirstTime();
        LocalDateTime startDate = appointmentRequest != null ? appointmentRequest.getStartDate() : appointmentDetails.getStartDate();
        LocalDateTime endDate = appointmentRequest != null ? appointmentRequest.getEndDate() : appointmentDetails.getEndDate();
        String appointmentReason = appointmentRequest != null ? appointmentRequest.getReasonForAppointment() : appointmentDetails.getAppointmentReason();

        appointmentDetailsHistory.setAppointment(appointment);
        appointmentDetailsHistory.setAppointmentDetails(appointmentDetails);
        appointmentDetailsHistory.setFirstName(patient.getFirstName());
        appointmentDetailsHistory.setLastName(patient.getLastName());
        appointmentDetailsHistory.setAddress(patient.getAddress());
        appointmentDetailsHistory.setGender(patient.getGender());
        appointmentDetailsHistory.setFirstTime(firstTime);
        appointmentDetailsHistory.setStartDate(startDate);
        appointmentDetailsHistory.setEndDate(endDate);
        appointmentDetailsHistory.setMobileNo(patient.getMobileNo());
        appointmentDetailsHistory.setEmail(patient.getUsers().getEmail());
        appointmentDetailsHistory.setAppointmentReason(appointmentReason);
        appointmentDetailsHistory.setCreated(LocalDateTime.now());
        appointmentDetailsHistory.setModified(LocalDateTime.now());

        return appointmentDetailsHistory;
    }

}