package io.muffin.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users users;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="mobile_no")
    private String mobileNo;
    @Column(name="birth_date")
    private LocalDate birthDate;
    @Column(name="gender")
    private int gender;
    @Column(name="address")
    private String address;
    @Column(name="profile_image")
    private String profileImage;
    @Column(name="doctor_code_id")
    private String doctorCodeId;
    @Column(name="doctor_description")
    private String doctorDescription;
    @Column(name="no_of_years_experience")
    private Integer noOfYearsExperience;
    @Column(name="education")
    private String education;
    @Column(name="schedule")
    private String schedule;
    @Column(name="expertise")
    private String expertise;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "modified")
    private LocalDateTime modified;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;
}
