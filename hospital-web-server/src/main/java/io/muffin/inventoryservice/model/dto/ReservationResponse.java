package io.muffin.inventoryservice.model.dto;

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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String updatedBy;
}
