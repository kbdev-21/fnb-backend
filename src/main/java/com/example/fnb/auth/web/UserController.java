package com.example.fnb.auth.web;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.security.SecurityUtil;
import com.example.fnb.auth.UserService;
import com.example.fnb.auth.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<Page<UserDto>> getUsers(
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "10") int pageSize,
        @RequestParam(required = false, defaultValue = "-createdAt") String sortBy
    ) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.ok(userService.getUsers(pageNumber, pageSize, sortBy));
    }

    @GetMapping("/api/users/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        var currentRole = SecurityUtil.getCurrentUserRole().orElseThrow(
            () -> new DomainException(DomainExceptionCode.USER_NOT_ALLOWED)
        );
        if(!currentRole.equals(UserRole.ADMIN)) {
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
