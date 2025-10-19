package com.example.fnb.user;

import com.example.fnb.user.dto.CreateUserDto;
import com.example.fnb.user.dto.UserAuthDataDto;
import com.example.fnb.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(CreateUserDto request);
    List<UserDto> getUsers();
    UserDto getUserById(UUID id);
    UserAuthDataDto getUserAuthDataByPhoneNumOrEmail(String phoneNumOrEmail);
    UserDto assignUserAsStaff(UUID userId, String storeCode);
}
