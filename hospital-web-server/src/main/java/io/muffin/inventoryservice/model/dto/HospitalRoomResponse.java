package io.muffin.inventoryservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalRoomResponse {

    private Long id;
    private String roomCode;
    private String roomName;
    private String description;
    private String roomImage;
    private String createdBy;
    private String updatedBy;
}
