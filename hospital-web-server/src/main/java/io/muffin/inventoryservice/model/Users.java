package io.muffin.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_type")
    private Integer userType;
    @Column(name = "registration_token")
    private String registrationToken;
    @Column(name = "datetime_password_reset")
    private LocalDateTime datetimePasswordReset;
    @Column(name = "reset_pass_token")
    private String resetPassToken;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @JoinTable(name = "user_authorities")
    @ManyToOne(cascade = CascadeType.ALL)
    private Authorities authorities;
    @Column(name = "is_confirmed")
    private boolean isConfirmed;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "modified")
    private LocalDateTime modified;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;
}
