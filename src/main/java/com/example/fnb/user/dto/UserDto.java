package com.example.fnb.user.dto;

import com.example.fnb.shared.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserDto {
    private UUID id;
    private String phoneNum;
    @Nullable
    private String email;
    private String firstName;
    private String lastName;
    private String normalizedName;
    private UserRole role;
    private Instant createdAt;
    private List<UserDtoAddress> address;

    @Getter
    @Setter
    public static class UserDtoAddress {
        private String type;
        private String city;
        private String detail;
    }
}
