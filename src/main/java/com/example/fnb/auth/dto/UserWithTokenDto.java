package com.example.fnb.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserWithTokenDto {
    public UserDto user;
    public String token;
}
