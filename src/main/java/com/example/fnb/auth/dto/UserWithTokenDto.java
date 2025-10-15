package com.example.fnb.auth.dto;

import com.example.fnb.user.dto.UserDto;
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
