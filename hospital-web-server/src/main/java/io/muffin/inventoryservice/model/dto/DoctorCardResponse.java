package io.muffin.inventoryservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorCardResponse {

    private String name;
    private String image;
    private String specialization;
    private String description;
}
