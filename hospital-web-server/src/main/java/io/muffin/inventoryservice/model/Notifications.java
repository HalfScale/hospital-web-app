package io.muffin.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private UserDetails receiver;
    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;
    private long status;
    private String message;
    private LocalDateTime viewed;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean deleted;
    private LocalDateTime deletedDate;
}
