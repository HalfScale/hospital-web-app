package io.muffin.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentResponse {

    private Long id;
    private int status;
    private AppointmentPatientDTO patient;
    private AppointmentDoctorDTO doctor;
    private AppointmentDetailsDTO appointmentDetails;
}
