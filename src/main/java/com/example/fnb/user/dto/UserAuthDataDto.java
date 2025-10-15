package com.example.fnb.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
public class UserAuthDataDto {
    private UUID id;
    private String phoneNum;
    @Nullable
    private String email;
    private String hashedPassword;
}
