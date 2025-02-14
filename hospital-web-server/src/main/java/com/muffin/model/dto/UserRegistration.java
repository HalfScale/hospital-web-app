package com.muffin.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffin.annotations.PasswordMatch;
import com.muffin.annotations.UniqueEmail;
import com.muffin.annotations.ValidHospitalCode;
import lombok.*;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.muffin.utility.ValidationUtil.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatch(groups = SecondOrder.class)
@GroupSequence({FirstOrder.class, SecondOrder.class, ThirdOrder.class, UserRegistration.class})
public class UserRegistration {

    @NotBlank(message = "First Name is Required", groups = FirstOrder.class)
    private String firstName;

    @NotBlank(message = "Last Name is Required", groups = FirstOrder.class)
    private String lastName;

    @NotBlank(message = "Email is Required", groups = FirstOrder.class)
    @Email(message = "Invalid email format", groups = SecondOrder.class)
    @UniqueEmail
    private String email;

    @NotBlank(message = "Mobile No. is Required", groups = FirstOrder.class)
    @Pattern(regexp = ".*(^[0-9]+$)", message = "Invalid Mobile No", groups = SecondOrder.class)
    @Size(min = 11, max = 13, message = "Mobile No. should be 11-13 digits", groups = ThirdOrder.class)
    private String mobileNo;

    private int gender;

    @NotBlank(message = "Password is Required", groups = FirstOrder.class)
    @Size(min = 6, max = 15, message = "Password should be 6-15 characters", groups = ThirdOrder.class)
    private String password;

    private String confirmPassword;

    @ValidHospitalCode
    private String hospitalCode;

    @JsonIgnore
    private boolean termsOfAgreement;
}
