package io.muffin.inventoryservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalRoomRequest {

    private Long id;
    private String roomCode;
    private String roomName;
    private String description;
    @JsonIgnore
    private Object image;
}
