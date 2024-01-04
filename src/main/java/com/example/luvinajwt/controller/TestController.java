package com.example.luvinajwt.controller;

import com.example.luvinajwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> userAuthen() {
        return ResponseEntity.accepted().body(userService.getCurrentUser());
    }

    @GetMapping("/admin")
    public ResponseEntity<?> adminAuthen() {
        return ResponseEntity.accepted().body(userService.getCurrentUser());
    }
}
