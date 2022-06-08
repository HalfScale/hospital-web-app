package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.dto.AppointmentRequest;
import io.muffin.inventoryservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final ObjectMapper objectMapper;
    private final AppointmentService appointmentService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAll() {
        log.info("GET => [{}]", "");
        return appointmentService.findAll();
    }

    @GetMapping(path = "/details/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findById(@PathVariable String appointmentId) {
        log.info("GET_APPOINTMENT_ID => [{}]", appointmentId);
        return appointmentService.findById(appointmentId);
    }

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAppointment(@RequestBody AppointmentRequest appointmentRequest) throws JsonProcessingException {
        log.info("CREATE_APPOINTMENT => [{}]", objectMapper.writeValueAsString(appointmentRequest));
        return appointmentService.createAppointment(appointmentRequest);
    }

    @PutMapping(path = "/edit/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editAppointment(@RequestBody AppointmentRequest appointmentRequest, @PathVariable String appointmentId) throws JsonProcessingException {
        log.info("EDIT_APPOINTMENT => [{}], ID => [{}]", objectMapper.writeValueAsString(appointmentRequest), appointmentId);
        return appointmentService.editAppointment(appointmentId, appointmentRequest);
    }

    @PutMapping(path = "/edit/status/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editAppointmentStatus(@PathVariable String appointmentId,
                                                        @RequestBody Map<String, String> editAppointmentStatusRequest) throws JsonProcessingException {
        log.info("EDIT_APPOINTMENT_STATUS => [{}]", objectMapper.writeValueAsString(editAppointmentStatusRequest));
        return appointmentService.editAppointmentStatus(appointmentId, Integer.valueOf(editAppointmentStatusRequest.get("statusCode")), editAppointmentStatusRequest);
    }
}