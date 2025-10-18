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

    @GetMapping("/api/users")
    ResponseEntity<List<UserDto>> getUsers() {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF);
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/api/users/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF, UserRole.CUSTOMER);
        if(SecurityUtil.getCurrentUserRole().equals(UserRole.CUSTOMER)) {
            SecurityUtil.onlyAllowUserId(userId);
        }
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/api/users/assign-staff/{userId}")
    ResponseEntity<UserDto> assignStaff(@PathVariable UUID userId, @RequestParam(required = true) String storeCode) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.ok(userService.assignUserAsStaff(userId, storeCode));
    }
}
