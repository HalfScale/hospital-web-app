package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.dto.CreateReservationRequest;
import io.muffin.inventoryservice.service.RoomReservationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RoomReservationsController {

    private final ObjectMapper objectMapper;
    private final RoomReservationsService roomReservationsService;

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createRoomReservation(@RequestBody CreateReservationRequest createReservationRequest) throws JsonProcessingException {
        log.info("CREATE_RESERVATION => [{}]", objectMapper.writeValueAsString(createReservationRequest));
        return roomReservationsService.createRoomReservation(createReservationRequest);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcGet() {
        log.info("GET => [{}]", "");
        return null;
    }

    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcPut() {
        log.info("PUT => [{}]", "");
        return null;
    }

    @DeleteMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcDelete() {
        log.info("DELETE => [{}]", "");
        return null;
    }
}