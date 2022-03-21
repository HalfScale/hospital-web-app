package io.muffin.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DoctorCodeValidationRequest {

    @NotBlank(message = "Doctor code is required!")
    private String doctorCode;
}
