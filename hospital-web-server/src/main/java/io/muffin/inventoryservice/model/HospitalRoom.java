package io.muffin.inventoryservice.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "hospital_room")
public class HospitalRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomCode;
    private String roomName;
    private String roomImage;
    private String description;
    private Long createdBy;
    private Long updatedBy;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private boolean deleted;
    private ZonedDateTime deletedDate;
}