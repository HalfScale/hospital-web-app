package io.muffin.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
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
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authorities_id", referencedColumnName = "id")
    )
    @ManyToOne(cascade = CascadeType.ALL)
    private Authorities authorities;
    @Column(name = "is_confirmed")
    private boolean isConfirmed;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "created", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime created;
    @Column(name = "modified", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime modified;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "deleted_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime deletedDate;
}
