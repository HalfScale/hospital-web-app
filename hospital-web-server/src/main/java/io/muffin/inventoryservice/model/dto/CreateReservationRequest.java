package io.muffin.inventoryservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.muffin.inventoryservice.model.HospitalRoom;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateReservationRequest {
    private String id;
    private String hospitalRoomId;
    private boolean hasAssociatedAppointmentId;
    private String associatedAppointmentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
