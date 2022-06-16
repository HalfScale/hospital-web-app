package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.service.NotificationsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationsController {

    private final ObjectMapper objectMapper;
    private final NotificationsService notificationsService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAll(Pageable pageable) {
        log.info("GET_ALL_NOTIFICATIONS");
        return notificationsService.findAll(pageable);
    }

    @PutMapping(path = "/viewed/{notificationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> setNotificationViewedDate(@PathVariable Long notificationId) {
        log.info("VIEWED_NOTIFICATION => [{}]", notificationId);
        return notificationsService.setNotificationViewedDate(notificationId);
    }

    @GetMapping(path = "/unviewed/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> countUnviewedNotifications() {
        log.info("GET_UNVIEWED_NOTIFICATIONS");
        return notificationsService.countUnviewedNotifications();
    }
}