package io.muffin.inventoryservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
public class ReservationResponse {

    private Long id;
    private HospitalRoomResponse hospitalRoomResponse;
    private String reservedByUsername;
    private Long reservedById;
    private boolean hasAssociatedAppointmentId;
    private Long associatedAppointmentId;
    private Integer reservationStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime endDate;
    private String updatedBy;
}
