package com.example.fnb.auth.web;

import com.example.fnb.auth.AuthService;
import com.example.fnb.auth.dto.LoginRequestDto;
import com.example.fnb.auth.dto.RegisterRequestDto;
import com.example.fnb.auth.dto.UserWithTokenDto;
import com.example.fnb.user.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<UserWithTokenDto> login(@Valid @RequestBody LoginRequestDto request) {
        UserWithTokenDto result = authService.loginWithPassword(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<UserWithTokenDto> register(@Valid @RequestBody RegisterRequestDto request) {
        UserWithTokenDto result = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<UserDto> me() {
        UserDto result = authService.me();
        return ResponseEntity.ok(result);
    }
}
