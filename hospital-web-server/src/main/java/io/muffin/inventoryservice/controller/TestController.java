package io.muffin.inventoryservice.controller;

import io.muffin.inventoryservice.utility.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<String> getAllTodos() {
        log.info("LOGGED_USER => [{}]", authUtil.getLoggedUserName());
        return ResponseEntity.ok("for admin role");
    }

}
