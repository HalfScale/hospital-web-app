package io.muffin.inventoryservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDetailsDTO {

    private String firstName;
    private String lastName;
    private String address;
    private int gender;
    private boolean firstTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private String mobileNo;
    private String email;
    private String reasonForAppointment;
    private String cancelReason;
}
