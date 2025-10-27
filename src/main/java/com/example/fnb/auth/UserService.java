package com.example.fnb.auth;

import com.example.fnb.auth.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> getUsers();
    UserDto getUserById(UUID id);
    UserDto assignUserAsStaff(UUID userId, String storeCode);
}
