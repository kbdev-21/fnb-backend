package com.example.fnb.auth.domain;

import com.example.fnb.auth.AuthService;
import com.example.fnb.auth.dto.LoginRequestDto;
import com.example.fnb.auth.dto.RegisterRequestDto;
import com.example.fnb.auth.dto.UserWithTokenDto;
import com.example.fnb.auth.event.RegisterSuccessEvent;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.security.JwtProvider;
import com.example.fnb.shared.security.SecurityUtil;
import com.example.fnb.shared.utils.StringUtil;
import com.example.fnb.auth.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public AuthServiceImpl(
        UserRepository userRepository,
        JwtProvider jwtProvider,
        PasswordEncoder passwordEncoder, ModelMapper modelMapper,
        ApplicationEventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserWithTokenDto loginWithPassword(LoginRequestDto request) {
        var user = userRepository.findByPhoneNum(request.getPhoneNumOrEmail())
            .or(() -> userRepository.findByEmail(request.getPhoneNumOrEmail()))
            .orElseThrow(() -> new DomainException(DomainExceptionCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), user.getHashedPassword())) {
            throw new DomainException(DomainExceptionCode.WRONG_CREDENTIALS);
        }

        var expiresInSeconds = user.getRole() == UserRole.CUSTOMER ? 12*60*60 : 4*60*60;
        var token = jwtProvider.generateToken(user.getId(), expiresInSeconds);

        return new UserWithTokenDto(modelMapper.map(user, UserDto.class), token);
    }

    @Override
    public UserWithTokenDto register(RegisterRequestDto request) {
        var normalizedName = StringUtil.normalizeVietnamese(request.getFirstName() + " " + request.getLastName());
        var email = request.getEmail();
        var hashedPassword = passwordEncoder.encode(request.getPassword());

        /* TODO: fix this cheating later */
        var role = UserRole.CUSTOMER;
        if(email.equals("doankimbang210703@gmail.com") || (email.equals("phananhkiet2k5@gmail.com"))) {
            role = UserRole.ADMIN;
        }

        var newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setPhoneNum(request.getPhoneNum());
        newUser.setEmail(email);
        newUser.setHashedPassword(hashedPassword);
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setNormalizedName(normalizedName);
        newUser.setStaffOfStoreCode(null);
        newUser.setRole(role);
        newUser.setCreatedAt(Instant.now());
        newUser.setAddresses(new ArrayList<>());

        var savedUser = userRepository.save(newUser);

        var expiresInSeconds = savedUser.getRole() == UserRole.CUSTOMER ? 672*60*60 : 4*60*60;
        var token = jwtProvider.generateToken(savedUser.getId(), expiresInSeconds);

        var userDto = modelMapper.map(savedUser, UserDto.class);

        eventPublisher.publishEvent(new RegisterSuccessEvent(this, userDto));
        return new UserWithTokenDto(userDto, token);
    }

    @Override
    public UserDto me() {
        var currentUserId = SecurityUtil.getCurrentUserId().orElseThrow(
            () -> new DomainException(DomainExceptionCode.UNAUTHORIZED)
        );
        var user = userRepository.findById(currentUserId).orElseThrow(
            () -> new DomainException(DomainExceptionCode.USER_NOT_FOUND)
        );
        return modelMapper.map(user, UserDto.class);
    }
}
