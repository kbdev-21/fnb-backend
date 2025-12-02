package com.example.fnb.auth.dto;

import org.jetbrains.annotations.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String avtUrl;
}
