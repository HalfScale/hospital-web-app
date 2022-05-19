package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.dto.ReservationRequest;
import io.muffin.inventoryservice.service.RoomReservationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/roomReservation")
public class RoomReservationsController {

    private final ObjectMapper objectMapper;
    private final RoomReservationsService roomReservationsService;

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createRoomReservation(@RequestBody ReservationRequest reservationRequest) throws JsonProcessingException {
        log.info("CREATE_RESERVATION => [{}]", objectMapper.writeValueAsString(reservationRequest));
        return roomReservationsService.createRoomReservation(reservationRequest);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAllRoomReservation(@RequestParam String roomCode, @RequestParam String roomName,
                                                         @RequestParam String status, Pageable pageable) {
        log.info("FIND_ALL => [{}]", "");
        return null;
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateRoomReservation(@RequestBody ReservationRequest reservationRequest) throws JsonProcessingException {
        log.info("PUT => [{}]", objectMapper.writeValueAsString(reservationRequest));
        return roomReservationsService.updateRoomReservation(reservationRequest);
    }

    @DeleteMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcDelete() {
        log.info("DELETE => [{}]", "");
        return null;
    }
}