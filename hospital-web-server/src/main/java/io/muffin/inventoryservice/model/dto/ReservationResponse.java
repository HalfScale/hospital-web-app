package io.muffin.inventoryservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationResponse {

    private Long id;
    private HospitalRoomResponse hospitalRoomResponse;
    private String reservedByUsername;
    private boolean hasAssociatedAppointmentId;
    private Long associatedAppointmentId;
    private Integer reservationStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private String updatedBy;
}
