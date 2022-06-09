package io.muffin.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentPatientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private int gender;
    private String mobileNo;
    private String email;
}
