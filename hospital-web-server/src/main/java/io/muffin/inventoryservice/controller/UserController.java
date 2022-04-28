package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.dto.UserProfileRequest;
import io.muffin.inventoryservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PutMapping(path = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUserProfile(@RequestParam("updateData") String profileDto,
                                                    @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        log.info("USER_UPDATE data => [{}]", profileDto);
        log.info("USER_UPDATE file => [{}]", file);
        userService.updateUserProfile(profileDto, file);
        return null;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcGet() {
        log.info("GET => [{}]", "");
        return null;
    }

}
