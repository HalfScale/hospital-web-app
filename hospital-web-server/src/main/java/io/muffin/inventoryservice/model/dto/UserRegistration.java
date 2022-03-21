package io.muffin.inventoryservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistration {

    @NotBlank(message="First Name is Required")
    private String firstName;

    @NotBlank(message="Last Name is Required")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Email(message="Invalid email format")
//    @UniqueEmail(message="Email is already in use",groups = ThirdLevel.class)
    private String email;

    @NotBlank(message = "Mobile No. is Required")
//    @Pattern(regexp = ".*(^[0-9]+$)", message = "Invalid Mobile No", groups = SecondLevel.class)
//    @Size(min = 11, max = 13, message = "Mobile No. should be 11-13 digits", groups = ThirdLevel.class)
    private String mobileNo;

    private int gender;

    @NotBlank(message="Password is Required")
//    @Size(min = 6, max = 15, message = "Password should be 6-15 characters", groups = SecondLevel.class)
    private String password;

    @NotBlank(message="Confirm password is Required")
//    @Size(min = 6, max = 15, message = "Password should be 6-15 characters", groups = SecondLevel.class)
    private String confirmPassword;

//    @ValidHospitalCode
    private String hospitalCode;

    @JsonIgnore
    private String termsOfAgreement;
}
