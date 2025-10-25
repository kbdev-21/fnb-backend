package com.example.fnb.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class CreateUserDto {
    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{1,3}?[- ]?[0-9]{3,4}[- ]?[0-9]{3,4}$")
    private String phoneNum;

    @Email
    private String email;

    private String hashedPassword;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
}
