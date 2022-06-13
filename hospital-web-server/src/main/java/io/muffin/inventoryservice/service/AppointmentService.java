package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.exception.AuthenticationException;
import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.jwt.JwtUserDetails;
import io.muffin.inventoryservice.model.*;
import io.muffin.inventoryservice.model.dto.*;
import io.muffin.inventoryservice.repository.*;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import io.muffin.inventoryservice.utility.SystemUtil;
import org.apache.tomcat.jni.Local;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        AppointmentDetails appointmentDetails = appointmentDetailsRepository.findByAppointmentId(Long.valueOf(id))
                .orElseThrow(() -> new HospitalException("Appointment does not exist!"));

        Appointment appointment = appointmentDetails.getAppointment();
        UserDetails patient = appointment.getPatient();
        UserDetails doctor = appointment.getDoctor();

        AppointmentPatientDTO appointmentPatientDTO = this.mapToAppointmentPatientDTO(patient);

        AppointmentDoctorDTO appointmentDoctorDTO = this.mapToAppointmentDoctorDTO(doctor);

        AppointmentDetailsDTO appointmentDetailsDTO = this.mapToAppointmentDetailsDTO(appointmentDetails);

        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setId(appointment.getId());
        appointmentResponse.setStatus(appointment.getAppointmentStatus());
        appointmentResponse.setPatient(appointmentPatientDTO);
        appointmentResponse.setDoctor(appointmentDoctorDTO);
        appointmentResponse.setAppointmentDetails(appointmentDetailsDTO);

        return ResponseEntity.ok(appointmentResponse);
    }

    public ResponseEntity<Object> findAll(Long appointmentId, String username, Pageable pageable) {
        // extract all appointment based on the logged user
        // then give the common response.
        // UI will extract the data based on the logged user in the client.
        JwtUserDetails currentJwtUserDetails = authUtil.getCurrentUser();
        Long currentUserId = currentJwtUserDetails.getId();
        Optional<? extends GrantedAuthority> authorities = currentJwtUserDetails.getAuthorities().stream().findFirst();
        String authority = authorities.get().getAuthority();

        Page<AppointmentResponse> response = null;

        log.info("currentUserId => [{}]", currentUserId);
        log.info("appointmentId => [{}]", appointmentId);
        log.info("username => [{}]", username);

        if(authority.equals("PATIENT")) {
            response = appointmentDetailsRepository.findAllAppointmentsByDoctor(currentUserId, appointmentId, username, pageable)
                    .map(appointmentDetails -> {
                        Appointment appointment = appointmentDetails.getAppointment();
                        UserDetails patient = appointment.getPatient();
                        UserDetails doctor = appointment.getDoctor();

                        AppointmentPatientDTO appointmentPatientDTO = this.mapToAppointmentPatientDTO(patient);

                        AppointmentDoctorDTO appointmentDoctorDTO = this.mapToAppointmentDoctorDTO(doctor);

                        AppointmentDetailsDTO appointmentDetailsDTO = this.mapToAppointmentDetailsDTO(appointmentDetails);

                        AppointmentResponse appointmentResponse = new AppointmentResponse();
                        appointmentResponse.setId(appointment.getId());
                        appointmentResponse.setStatus(appointment.getAppointmentStatus());
                        appointmentResponse.setPatient(appointmentPatientDTO);
                        appointmentResponse.setDoctor(appointmentDoctorDTO);
                        appointmentResponse.setAppointmentDetails(appointmentDetailsDTO);
                        return appointmentResponse;
                    });
        }else {
            response = appointmentDetailsRepository.findAllAppointmentsByPatient(currentUserId, Long.valueOf(appointmentId), username, pageable)
                    .map(appointmentDetails -> {
                        Appointment appointment = appointmentDetails.getAppointment();
                        UserDetails patient = appointment.getPatient();
                        UserDetails doctor = appointment.getDoctor();

                        AppointmentPatientDTO appointmentPatientDTO = this.mapToAppointmentPatientDTO(patient);

                        AppointmentDoctorDTO appointmentDoctorDTO = this.mapToAppointmentDoctorDTO(doctor);

                        AppointmentDetailsDTO appointmentDetailsDTO = this.mapToAppointmentDetailsDTO(appointmentDetails);

                        AppointmentResponse appointmentResponse = new AppointmentResponse();
                        appointmentResponse.setId(appointment.getId());
                        appointmentResponse.setStatus(appointment.getAppointmentStatus());
                        appointmentResponse.setPatient(appointmentPatientDTO);
                        appointmentResponse.setDoctor(appointmentDoctorDTO);
                        appointmentResponse.setAppointmentDetails(appointmentDetailsDTO);
                        return appointmentResponse;
                    });
        }


        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(response));
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

            if (status == Constants.APPOINTMENT_REJECTED) {
                AppointmentDetailsHistory appointmentDetailsHistory = this.mapToAppointmentDetailsHistory(appointment, appointmentDetails, null,
                        appointment.getPatient());

                String reason = editAppointmentStatusRequest.get("reason");
                appointmentDetails.setCancelReason(reason);
                appointmentDetailsHistory.setCancelReason(reason);
                appointmentDetails.setModified(LocalDateTime.now());

                appointmentDetailsRepository.save(appointmentDetails);
                appointmentDetailsHistoryRepository.save(appointmentDetailsHistory);
            }
        }

        if (status == Constants.APPOINTMENT_CANCELLED) {

            if (userType != Constants.USER_PATIENT) {
                throw new AuthenticationException("Unauthorized user to perform action!");
            }

            String reason = editAppointmentStatusRequest.get("reason");

            appointment.setAppointmentStatus(status);
            appointment.setModified(LocalDateTime.now());

            appointmentRepository.save(appointment);

            AppointmentHistory appointmentHistory = this.mapToAppointmentHistory(appointment, appointment.getPatient(), appointment.getDoctor());
            appointmentHistoryRepository.save(appointmentHistory);

            appointmentDetails.setModified(LocalDateTime.now());
            appointmentDetails.setCancelReason(reason);

            appointmentDetailsRepository.save(appointmentDetails);

            AppointmentDetailsHistory appointmentDetailsHistory = this.mapToAppointmentDetailsHistory(appointment, appointmentDetails, null, appointment.getPatient());
            appointmentDetailsHistory.setCancelReason(reason);
            appointmentDetailsHistoryRepository.save(appointmentDetailsHistory);
        }

        return ResponseEntity.ok(appointment.getId());
    }

    public ResponseEntity<Object> findDoctorAppointments(String startDate, String endDate, String doctorId, Pageable pageable) {
        DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedStartDate = LocalDateTime.parse(startDate, dateTimeFormatter);
        LocalDateTime parsedEndDate = LocalDateTime.parse(endDate, dateTimeFormatter);

        Page<AppointmentResponse> appointmentResponses = appointmentDetailsRepository
                .findDoctorAppointments(parsedStartDate, parsedEndDate, Long.valueOf(doctorId), pageable)
                .map(appointmentDetails -> {

                    Appointment appointment = appointmentDetails.getAppointment();
                    UserDetails patient = appointment.getPatient();
                    UserDetails doctor = appointment.getDoctor();

                    AppointmentPatientDTO appointmentPatientDTO = this.mapToAppointmentPatientDTO(patient);

                    AppointmentDoctorDTO appointmentDoctorDTO = this.mapToAppointmentDoctorDTO(doctor);

                    AppointmentDetailsDTO appointmentDetailsDTO = this.mapToAppointmentDetailsDTO(appointmentDetails);

                    AppointmentResponse appointmentResponse = new AppointmentResponse();
                    appointmentResponse.setId(appointment.getId());
                    appointmentResponse.setStatus(appointment.getAppointmentStatus());
                    appointmentResponse.setPatient(appointmentPatientDTO);
                    appointmentResponse.setDoctor(appointmentDoctorDTO);
                    appointmentResponse.setAppointmentDetails(appointmentDetailsDTO);

                    return appointmentResponse;
                });
        return ResponseEntity.ok(SystemUtil.mapToGenericPageableResponse(appointmentResponses));
    }

    private AppointmentDetailsDTO mapToAppointmentDetailsDTO(AppointmentDetails appointmentDetails) {
        AppointmentDetailsDTO appointmentDetailsDTO = new AppointmentDetailsDTO();
        appointmentDetailsDTO.setFirstName(appointmentDetails.getFirstName());
        appointmentDetailsDTO.setLastName(appointmentDetails.getLastName());
        appointmentDetailsDTO.setAddress(appointmentDetails.getAddress());
        appointmentDetailsDTO.setGender(appointmentDetails.getGender());
        appointmentDetailsDTO.setFirstTime(appointmentDetails.isFirstTime());
        appointmentDetailsDTO.setStartDate(appointmentDetails.getStartDate());
        appointmentDetailsDTO.setEndDate(appointmentDetails.getEndDate());
        appointmentDetailsDTO.setEmail(appointmentDetails.getEmail());
        appointmentDetailsDTO.setMobileNo(appointmentDetails.getMobileNo());
        appointmentDetailsDTO.setReasonForAppointment(appointmentDetails.getAppointmentReason());
        appointmentDetailsDTO.setCancelReason(appointmentDetails.getCancelReason());
        return appointmentDetailsDTO;
    }

    private AppointmentPatientDTO mapToAppointmentPatientDTO(UserDetails patient) {
        AppointmentPatientDTO appointmentPatientDTO = new AppointmentPatientDTO();

        appointmentPatientDTO.setId(patient.getUsers().getId());
        appointmentPatientDTO.setFirstName(patient.getFirstName());
        appointmentPatientDTO.setLastName(patient.getLastName());
        appointmentPatientDTO.setAddress(patient.getAddress());
        appointmentPatientDTO.setGender(patient.getGender());
        appointmentPatientDTO.setMobileNo(patient.getMobileNo());
        appointmentPatientDTO.setEmail(patient.getUsers().getEmail());

        return appointmentPatientDTO;
    }

    private AppointmentDoctorDTO mapToAppointmentDoctorDTO(UserDetails doctor) {
        AppointmentDoctorDTO appointmentDoctorDTO = new AppointmentDoctorDTO();
        appointmentDoctorDTO.setId(doctor.getUsers().getId());
        appointmentDoctorDTO.setFirstName(doctor.getFirstName());
        appointmentDoctorDTO.setLastName(doctor.getLastName());
        appointmentDoctorDTO.setEmail(doctor.getUsers().getEmail());
        return appointmentDoctorDTO;
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