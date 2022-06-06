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
    @OneToOne
    @JoinColumn(name = "appointment_details_id", referencedColumnName = "id")
    private AppointmentDetails appointmentDetails;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean deleted;
    private LocalDateTime deletedDate;
}