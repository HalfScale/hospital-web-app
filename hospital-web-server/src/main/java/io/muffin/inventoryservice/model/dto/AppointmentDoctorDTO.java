package io.muffin.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDoctorDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
