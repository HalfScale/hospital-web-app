package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.HospitalRoom;
import io.muffin.inventoryservice.model.RoomReservations;
import io.muffin.inventoryservice.model.dto.CreateReservationRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.muffin.inventoryservice.repository.RoomReservationsRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomReservationsService {

    private final RoomReservationsRepository RoomReservationsRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public ResponseEntity<Object> findById(String id) {
        return null;
    }

    public ResponseEntity<Object> findAll() {
        return null;
    }

    public ResponseEntity<Object> createRoomReservation(CreateReservationRequest createReservationRequest) throws JsonProcessingException {
        RoomReservations roomReservations = modelMapper.map(createReservationRequest, RoomReservations.class);
        log.info("CREATING_RESERVATION => [{}]", objectMapper.writeValueAsString(roomReservations));
        return null;
    }
}
