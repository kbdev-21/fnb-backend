package com.example.fnb.user.domain;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.utils.SecurityUtil;
import com.example.fnb.shared.utils.StringUtil;
import com.example.fnb.user.UserService;
import com.example.fnb.user.dto.CreateUserRequestDto;
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
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto createUser(CreateUserRequestDto request) {
        var normalizedName = StringUtil.normalizeVietnamese(request.getFirstName() + " " + request.getLastName());

        var addresses = new ArrayList<User.Address>();
        var testAddress = new User.Address();
        testAddress.setType("Nhà riêng");
        testAddress.setCity("HCM");
        testAddress.setDetail("100 Trần Hưng Đạo");
        addresses.add(testAddress);

        var newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setPhoneNum(request.getPhoneNum());
        newUser.setEmail(request.getEmail());
        newUser.setHashedPassword(request.getHashedPassword());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setNormalizedName(normalizedName);
        newUser.setRole(UserRole.CUSTOMER);
        newUser.setCreatedAt(Instant.now());
        newUser.setAddresses(addresses);

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

    private UserDto entityToDto(User user) {
        var userDto = modelMapper.map(user, UserDto.class);
        userDto.setAddress(user.getAddresses().stream().map(a ->
            modelMapper.map(a, UserDto.UserDtoAddress.class)).toList());
        return userDto;
    }
}
