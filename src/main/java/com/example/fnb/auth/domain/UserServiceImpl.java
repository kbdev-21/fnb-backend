package com.example.fnb.auth.domain;

import com.example.fnb.auth.dto.UpdateUserDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.utils.AppUtil;
import com.example.fnb.shared.utils.StringUtil;
import com.example.fnb.store.StoreService;
import com.example.fnb.auth.UserService;
import com.example.fnb.auth.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
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
    public Page<UserDto> getUsers(int pageNumber, int pageSize, String orderBy, String searchKey) {
        var sort = AppUtil.createSort(orderBy);
        var pageable = PageRequest.of(pageNumber, pageSize, sort);

        var specification = UserSpecification.search(searchKey);

        var users = userRepository.findAll(specification, pageable);

        var userDtos = users.getContent().stream().map(this::entityToDto).toList();
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
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

    @Override
    public UserDto updateUserById(UUID id, UpdateUserDto dto) {
        var user = userRepository.findById(id).orElseThrow(
            () -> new DomainException(DomainExceptionCode.USER_NOT_FOUND)
        );

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        if (dto.getAvtUrl() != null) {
            user.setAvtUrl(dto.getAvtUrl());
        }

        if (dto.getFirstName() != null || dto.getLastName() != null) {
            var normalized = StringUtil.normalizeVietnamese(user.getFirstName() + " " + user.getLastName());
            user.setNormalizedName(normalized);
        }

        var savedUser = userRepository.save(user);
        return entityToDto(savedUser);
    }

    private UserDto entityToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
