package com.example.fnb.auth;

import com.example.fnb.auth.dto.UserDto;
import com.example.fnb.shared.enums.UserRole;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserService {
    Page<UserDto> getUsers(int pageNumber, int pageSize, String sortBy, String searchKey);
    UserDto getUserById(UUID id);
    UserDto assignUserAsStaff(UUID userId, String storeCode);
}
