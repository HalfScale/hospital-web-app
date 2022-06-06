package io.muffin.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDoctorDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
}
