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
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean deleted;
    private LocalDateTime deletedDate;
}