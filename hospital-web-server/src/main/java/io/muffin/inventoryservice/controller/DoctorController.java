package io.muffin.inventoryservice.controller;

import io.muffin.inventoryservice.model.dto.DoctorCardResponse;
import io.muffin.inventoryservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<DoctorCardResponse> findAllDoctors(@RequestParam(required = false) String doctorCode,
                                                   @RequestParam(required = false) String name,
                                                   Pageable pageable) {
        log.info("DOCTOR_LIST");
        log.info("DOCTOR_CODE => [{}]", doctorCode);
        log.info("DOCTOR_NAME => [{}]", name);
        return doctorService.findAllDoctor(name, doctorCode, pageable);
    }
}
