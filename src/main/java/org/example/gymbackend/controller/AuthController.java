package org.example.gymbackend.controller;

import jakarta.validation.Valid;
import org.example.gymbackend.dto.request.LoginRequest;
import org.example.gymbackend.dto.request.RegisterRequest;
import org.example.gymbackend.dto.response.AuthResponse;
import org.example.gymbackend.service.AuthService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}