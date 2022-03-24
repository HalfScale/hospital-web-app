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
    @JsonIgnore
    private String specialization;
    @JsonIgnore
    private String doctorCodeId;
    private String doctorCode;
    private Integer noOfYearsExperience;
    private String education;
    private String schedule;
    private String expertise;
}
