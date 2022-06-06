package io.muffin.inventoryservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequest {

    private Long id;
    private Long doctorId;
    private String address;
    private boolean isFirstTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reasonForAppointment;
}
