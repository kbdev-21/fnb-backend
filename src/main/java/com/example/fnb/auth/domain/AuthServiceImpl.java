package com.example.fnb.auth.domain;

import com.example.fnb.auth.AuthService;
import com.example.fnb.auth.dto.LoginRequestDto;
import com.example.fnb.auth.dto.RegisterRequestDto;
import com.example.fnb.auth.dto.UserWithTokenDto;
import com.example.fnb.auth.RegisterSuccessEvent;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.security.JwtProvider;
import com.example.fnb.shared.utils.SecurityUtil;
import com.example.fnb.user.UserService;
import com.example.fnb.user.dto.CreateUserDto;
import com.example.fnb.user.dto.UserDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    public AuthServiceImpl(
        UserService userService,
        JwtProvider jwtProvider,
        PasswordEncoder passwordEncoder,
        ApplicationEventPublisher eventPublisher
    ) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserWithTokenDto loginWithPassword(LoginRequestDto request) {
        var userAuthData = userService.getUserAuthDataByPhoneNumOrEmail(
            request.getPhoneNumOrEmail()
        );

        if(!passwordEncoder.matches(request.getPassword(), userAuthData.getHashedPassword())) {
            throw new DomainException(DomainExceptionCode.WRONG_CREDENTIALS);
        }

        var user = userService.getUserById(userAuthData.getId());
        var expiresInSeconds = user.getRole() == UserRole.CUSTOMER ? 12*60*60 : 4*60*60;
        var token = jwtProvider.generateToken(user.getId(), user.getRole(), user.getStaffOfStoreCode(), expiresInSeconds);

        return new UserWithTokenDto(user, token);
    }

    @Override
    public UserWithTokenDto register(RegisterRequestDto request) {
        var hashedPassword = passwordEncoder.encode(request.getPassword());

        var createUserRequest = new CreateUserDto();
        createUserRequest.setPhoneNum(request.getPhoneNum());
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setHashedPassword(hashedPassword);
        createUserRequest.setFirstName(request.getFirstName());
        createUserRequest.setLastName(request.getLastName());

        var newUser = userService.createUser(createUserRequest);

        var expiresInSeconds = newUser.getRole() == UserRole.CUSTOMER ? 12*60*60 : 4*60*60;
        var token = jwtProvider.generateToken(newUser.getId(), newUser.getRole(), newUser.getStaffOfStoreCode(), expiresInSeconds);

        eventPublisher.publishEvent(new RegisterSuccessEvent(this, newUser));

        return new UserWithTokenDto(newUser, token);
    }

    @Override
    public UserDto me() {
        var currentUserId = SecurityUtil.getCurrentUserId();
        return userService.getUserById(currentUserId);
    }
}
