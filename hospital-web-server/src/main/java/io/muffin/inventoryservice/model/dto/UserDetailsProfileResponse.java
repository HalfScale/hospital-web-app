package io.muffin.inventoryservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.muffin.inventoryservice.model.Users;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserDetailsProfileResponse {

    private Long id;
    private UserProfileResponse users;
    private String firstName;
    private String lastName;
    private String mobileNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private int gender;
    private String address;
    private String profileImage;
    private String doctorCodeId;
    private String specialization;
    private String doctorDescription;
    private Integer noOfYearsExperience;
    private String education;
    private String schedule;
    private String expertise;
}
