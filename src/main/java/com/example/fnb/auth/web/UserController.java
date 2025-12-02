package com.example.fnb.auth.web;

import com.example.fnb.auth.dto.UpdateUserDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.security.SecurityUtil;
import com.example.fnb.auth.UserService;
import com.example.fnb.auth.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        @RequestParam(required = false, defaultValue = "20") int pageSize,
        @RequestParam(required = false, defaultValue = "-createdAt") String sortBy,
        @RequestParam(required = false, defaultValue = "") String searchKey
    ) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.ok(userService.getUsers(pageNumber, pageSize, sortBy, searchKey));
    }

    @GetMapping("/api/users/{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        var currentRole = SecurityUtil.getCurrentUserRole().orElseThrow(
            () -> new DomainException(DomainExceptionCode.ACCESS_DENIED)
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

    @PatchMapping("/api/users/{userId}")
    ResponseEntity<UserDto> updateUser(
        @PathVariable UUID userId,
        @RequestBody UpdateUserDto dto
    ) {
        SecurityUtil.onlyAllowUserId(userId);
        return ResponseEntity.ok(userService.updateUserById(userId, dto));
    }
}
