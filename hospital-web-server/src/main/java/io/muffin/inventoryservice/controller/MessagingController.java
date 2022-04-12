package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.dto.MessagingRequest;
import io.muffin.inventoryservice.service.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/message")
public class MessagingController {

    private final MessagingService messagingService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendMessage(@RequestBody MessagingRequest messagingRequest) throws JsonProcessingException {
        log.info("SEND_MESSAGE_REQUEST => [{}]", objectMapper.writeValueAsString(messagingRequest));
        return messagingService.sendMesage(messagingRequest);
    }

    @GetMapping(path = "/thread/{receiverId}")
    public ResponseEntity<Object> getThread(@PathVariable String receiverId, Pageable pageable) throws JsonProcessingException {
        log.info("GET_THREAD => [{}]", objectMapper.writeValueAsString(pageable));
        return messagingService.getThread(receiverId, pageable);
    }
}
