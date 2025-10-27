package com.example.fnb.auth.domain;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.store.StoreService;
import com.example.fnb.auth.UserService;
import com.example.fnb.auth.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final StoreService storeService;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(StoreService storeService, UserRepository userRepository, ModelMapper modelMapper) {
        this.storeService = storeService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDto> getUsers() {
        var users = userRepository.findAll();
        return users.stream().map(this::entityToDto).toList();
    }

    @Override
    public UserDto getUserById(UUID id) {
        var user = userRepository.findById(id).orElseThrow(
            () -> new DomainException(DomainExceptionCode.USER_NOT_FOUND)
        );
        return entityToDto(user);
    }

    @Override
    public UserDto assignUserAsStaff(UUID userId, String storeCode) {
        var user = userRepository.findById(userId).orElseThrow(
            () -> new DomainException(DomainExceptionCode.USER_NOT_FOUND)
        );
        var store = storeService.getStoreByCode(storeCode);

        if(user.getEmail() == null || user.getHashedPassword() == null) {
            throw new DomainException(DomainExceptionCode.USER_IS_UNVERIFIED);
        }

        user.setRole(UserRole.STAFF);
        user.setStaffOfStoreCode(store.getCode());

        var savedUser = userRepository.save(user);
        return entityToDto(savedUser);
    }

    private UserDto entityToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
