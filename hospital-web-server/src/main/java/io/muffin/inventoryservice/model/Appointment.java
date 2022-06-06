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
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private UserDetails patient;
    @OneToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private UserDetails doctor;
    private Integer appointmentStatus;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean deleted;
    private LocalDateTime deletedDate;
}