package io.muffin.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentResponse {

    private Integer id;
    private AppointmentPatientDTO patient;
    private AppointmentDoctorDTO doctor;
    private boolean isFirstTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reasonForAppointment;
    private String cancelReason;
}
