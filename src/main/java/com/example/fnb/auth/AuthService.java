package com.example.fnb.auth;

import com.example.fnb.auth.dto.LoginRequestDto;
import com.example.fnb.auth.dto.RegisterRequestDto;
import com.example.fnb.auth.dto.UserWithTokenDto;
import com.example.fnb.user.dto.UserDto;

public interface AuthService {
    UserWithTokenDto loginWithPassword(LoginRequestDto request);
    UserWithTokenDto register(RegisterRequestDto request);
    UserDto me();
}
