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
@Table(name = "appointment_details_history")
public class AppointmentDetailsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;
    @ManyToOne
    @JoinColumn(name = "appointment_details_id", referencedColumnName = "id")
    private AppointmentDetails appointmentDetails;
    private String firstName;
    private String lastName;
    private String address;
    private int gender;
    private boolean firstTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String mobileNo;
    private String email;
    private String appointmentReason;
    private String cancelReason;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean deleted;
    private LocalDateTime deletedDate;
}