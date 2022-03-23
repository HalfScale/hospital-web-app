package io.muffin.inventoryservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserProfileRequest {

    private String firstName;
    private String lastName;
    private String mobileNo;
    private LocalDate birthDate;
    private String address;
    @JsonIgnore
    private Object image;
}
