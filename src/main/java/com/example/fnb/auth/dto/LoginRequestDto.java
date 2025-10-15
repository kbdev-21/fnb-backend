package com.example.fnb.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotBlank
    String phoneNumOrEmail;

    @NotBlank
    String password;
}
