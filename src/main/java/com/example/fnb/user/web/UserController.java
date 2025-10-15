package com.example.fnb.user.web;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.utils.SecurityUtil;
import com.example.fnb.user.UserService;
import com.example.fnb.user.dto.CreateUserRequestDto;
import com.example.fnb.user.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ResponseEntity<UserDto> create(@RequestBody @Valid CreateUserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/api/users/by-id/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        if(SecurityUtil.getCurrentUserRole().equals(UserRole.CUSTOMER)) {
            SecurityUtil.onlyAllowUserId(userId);
        }
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
