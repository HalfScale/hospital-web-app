package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.service.HospitalRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/hospitalRoom")
public class HospitalRoomController {

    private final ObjectMapper objectMapper;
    private final HospitalRoomService hospitalRoomService;

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addHospitalRoom(@RequestParam("hospitalRoomDto") String hospitalRoomDto,
                                                  @RequestParam(value = "file", required = false) MultipartFile image) throws JsonProcessingException {
        log.info("POST => [{}]", objectMapper.writeValueAsString(hospitalRoomDto));
        return hospitalRoomService.addHospitalRoom(hospitalRoomDto, image);
    }

    @GetMapping(path = "/rooms/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findByRoomId(@PathVariable String roomId) {
        log.info("FIND_BY_ROOM_ID => [{}]", roomId);
        return hospitalRoomService.findById(roomId);
    }

    @GetMapping(path = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAllHospitalRooms(@RequestParam(required = false) String roomCode,
                                                       @RequestParam(required = false) String roomName,
                                                       Pageable pageable) {
        log.info("FIND_ALL_ROOMS_BY_CODE_OR_NAME => [{}, {}]", roomCode, roomName);
        return hospitalRoomService.findAll(roomCode, roomName, pageable);
    }

    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcPut(@RequestParam("hospitalRoomDto") String hospitalRoomDto,
                                         @RequestParam(value = "file", required = false) MultipartFile image) throws JsonProcessingException {
        log.info("UPDATE_HOSPITAL_ROOM => [{}]", objectMapper.writeValueAsString(hospitalRoomDto));
        return hospitalRoomService.updateHospitalRoom(hospitalRoomDto, image);
    }

    @DeleteMapping(path = "/delete/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcDelete(@PathVariable String roomId) {
        log.info("DELETE_ROOM_BY_ID => [{}]", roomId);
        return hospitalRoomService.deleteHospitalRoom(roomId);
    }

    @GetMapping(path = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validateHospitalRoom(@RequestParam(required = false) Long roomId,
                                                        @RequestParam(required = false) String roomName,
                                                       @RequestParam(required = false) String roomCode) {
        log.info("VALIDATE ROOM_ID => [{}], ROOM_CODE => [{}], ROOM_NAME => [{}]", roomId, roomCode, roomName);
        return hospitalRoomService.validateHospitalRoom(roomId, roomCode, roomName);
    }
}