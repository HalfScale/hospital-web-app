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
    public ResponseEntity<Object> findAllHospitalRooms(Pageable pageable) {
//        log.info("FIND_BY_ROOM_ID => [{}]", roomId);
        return hospitalRoomService.findAll(pageable);
    }

    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcPut() {
        log.info("GET => [{}]", "");
        return null;
    }

    @DeleteMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcDelete() {
        log.info("GET => [{}]", "");
        return null;
    }
}