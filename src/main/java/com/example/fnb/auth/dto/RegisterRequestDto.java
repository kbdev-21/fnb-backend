package com.example.fnb.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.Nullable;

@Getter
public class RegisterRequestDto {
    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{1,3}?[- ]?[0-9]{3,4}[- ]?[0-9]{3,4}$")
    private String phoneNum;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 6)
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
}
