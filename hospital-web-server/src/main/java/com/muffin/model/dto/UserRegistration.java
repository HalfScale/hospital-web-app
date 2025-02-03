package com.muffin.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffin.annotations.PasswordMatch;
import com.muffin.annotations.UniqueEmail;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatch
public class UserRegistration {

    @NotBlank(message = "First Name is Required")
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email format")
    @UniqueEmail(message = "Email is already in use")
    private String email;

    @NotBlank(message = "Mobile No. is Required")
    @Pattern(regexp = ".*(^[0-9]+$)", message = "Invalid Mobile No")
    @Size(min = 11, max = 13, message = "Mobile No. should be 11-13 digits")
    private String mobileNo;

    private int gender;

    @NotBlank(message="Password is Required")
    @Size(min = 6, max = 15, message = "Password should be 6-15 characters")
    private String password;

    @NotBlank(message="Confirm password is Required")
    @Size(min = 6, max = 15, message = "Password should be 6-15 characters")
    private String confirmPassword;

//    @ValidHospitalCode
    private String hospitalCode;

    @JsonIgnore
    private String termsOfAgreement;
}
