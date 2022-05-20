package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.dto.ReservationRequest;
import io.muffin.inventoryservice.service.RoomReservationsService;
import io.muffin.inventoryservice.utility.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/roomReservation")
public class RoomReservationsController {

    private final ObjectMapper objectMapper;
    private final RoomReservationsService roomReservationsService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAllRoomReservation(@RequestParam(required = false) String roomCode, @RequestParam(required = false) String roomName,
                                                         @RequestParam(required = false) String status, Pageable pageable) {
        log.info("FIND_ALL => [{}], [{}], [{}]", roomCode, roomName, status);
        return roomReservationsService.findAll(roomCode, roomName, status, pageable);
    }

    @GetMapping(path = "/details/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAllRoomReservation(@PathVariable String reservationId) {
        log.info("FIND_RESERVATION_BY_ID => [{}]", reservationId);
        return roomReservationsService.findById(reservationId);
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createRoomReservation(@RequestBody ReservationRequest reservationRequest) throws JsonProcessingException {
        log.info("CREATE_RESERVATION => [{}]", objectMapper.writeValueAsString(reservationRequest));
        return roomReservationsService.createRoomReservation(reservationRequest);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateRoomReservation(@RequestBody ReservationRequest reservationRequest) throws JsonProcessingException {
        log.info("PUT => [{}]", objectMapper.writeValueAsString(reservationRequest));
        return roomReservationsService.updateRoomReservation(reservationRequest);
    }

    @PutMapping(path = "/update/done/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateRoomReservationToDone(@PathVariable String reservationId) throws JsonProcessingException {
        log.info("UPDATE_TO_DONE => [{}]", reservationId);
        return roomReservationsService.updateRoomReservationStatus(reservationId, Constants.RESERVATION_DONE);
    }

    @PutMapping(path = "/update/cancel/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateRoomReservationToCancelled(@PathVariable String reservationId) throws JsonProcessingException {
        log.info("UPDATE_TO_CANCELLED => [{}]", reservationId);
        return roomReservationsService.updateRoomReservationStatus(reservationId, Constants.RESERVATION_CANCELLED);
    }

    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcDelete() {
        log.info("DELETE => [{}]", "");
        return null;
    }
}