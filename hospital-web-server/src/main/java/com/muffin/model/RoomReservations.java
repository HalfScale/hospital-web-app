package com.muffin.model;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_reservations")
@Builder
public class RoomReservations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "hospital_room_id", referencedColumnName = "id")
    private HospitalRooms hospitalRooms;
    private String roomCode;
    private Long reservedByUserId;
    private boolean hasAssociatedAppointmentId;
    private Long associatedAppointmentId;
    private String reservationStatus;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Long updatedBy;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private boolean deleted;
    private ZonedDateTime deletedDate;

}