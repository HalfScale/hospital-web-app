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
@Table(name = "room_reservations")
public class RoomReservations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "hospital_room_id", referencedColumnName = "id")
    private HospitalRoom hospitalRoom;
    private String roomCode;
    private Long reservedByUserId;
    private boolean hasAssociatedAppointmentId;
    private Long associatedAppointmentId;
    private Integer reservationStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long updatedBy;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean deleted;
    private LocalDateTime deletedDate;

}