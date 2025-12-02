package com.example.fnb.auth.dto;

import com.example.fnb.shared.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String phoneNum;
    @Nullable
    private String email;
    private String firstName;
    private String lastName;
    private String normalizedName;
    private UserRole role;
    private String staffOfStoreCode;
    private Instant createdAt;
    private String avtUrl;
}
