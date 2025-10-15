package com.example.fnb.user.dto;

import com.example.fnb.shared.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class CreateUserRequestDto {
    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{1,3}?[- ]?[0-9]{3,4}[- ]?[0-9]{3,4}$")
    private String phoneNum;

    @Email
    @Nullable
    private String email;

    @Nullable
    private String hashedPassword;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
}
