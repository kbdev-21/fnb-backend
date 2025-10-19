package com.example.fnb.user.domain;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.utils.StringUtil;
import com.example.fnb.store.StoreService;
import com.example.fnb.user.UserService;
import com.example.fnb.user.dto.CreateUserDto;
import com.example.fnb.user.dto.UserAuthDataDto;
import com.example.fnb.user.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
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
    public UserDto createUser(CreateUserDto request) {
        var normalizedName = StringUtil.normalizeVietnamese(request.getFirstName() + " " + request.getLastName());
        var email = request.getEmail();

        /* TODO: fix this cheating later */
        var role = UserRole.CUSTOMER;
        if(email != null && email.equals("doankimbang210703@gmail.com")) {
            role = UserRole.ADMIN;
        }

        var newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setPhoneNum(request.getPhoneNum());
        newUser.setEmail(email);
        newUser.setHashedPassword(request.getHashedPassword());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setNormalizedName(normalizedName);
        newUser.setStaffOfStoreCode(null);
        newUser.setRole(role);
        newUser.setCreatedAt(Instant.now());
        newUser.setAddresses(new ArrayList<>());

        var savedUser = userRepository.save(newUser);

        return entityToDto(savedUser);
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
    public UserAuthDataDto getUserAuthDataByPhoneNumOrEmail(String phoneNumOrEmail) {
        var user = userRepository.findByPhoneNum(phoneNumOrEmail)
            .or(() -> userRepository.findByEmail(phoneNumOrEmail))
            .orElseThrow(() -> new DomainException(DomainExceptionCode.USER_NOT_FOUND));
        return modelMapper.map(user, UserAuthDataDto.class);
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
