package com.example.fnb.shared.security;

import com.example.fnb.shared.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticatedUser {
    private UUID id;
    private UserRole role;
    private String staffOfStoreCode;
}
