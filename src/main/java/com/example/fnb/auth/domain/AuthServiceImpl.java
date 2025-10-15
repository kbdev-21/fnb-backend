package com.example.fnb.auth.domain;

import com.example.fnb.auth.AuthService;
import com.example.fnb.auth.dto.LoginRequestDto;
import com.example.fnb.auth.dto.RegisterRequestDto;
import com.example.fnb.auth.dto.UserWithTokenDto;
import com.example.fnb.product.ProductService;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.security.JwtProvider;
import com.example.fnb.shared.utils.SecurityUtil;
import com.example.fnb.user.UserService;
import com.example.fnb.user.dto.CreateUserRequestDto;
import com.example.fnb.user.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final ProductService productService;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserService userService, ProductService productService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.productService = productService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserWithTokenDto loginWithPassword(LoginRequestDto request) {
        var product = productService.getProductById(UUID.randomUUID());
        System.out.println(product.getId() + ": " + product.getPrice());

        var userAuthData = userService.getUserAuthDataByPhoneNumOrEmail(
            request.getPhoneNumOrEmail()
        );

        if(!passwordEncoder.matches(request.getPassword(), userAuthData.getHashedPassword())) {
            throw new DomainException(DomainExceptionCode.WRONG_CREDENTIALS);
        }

        var user = userService.getUserById(userAuthData.getId());
        var expiresInSeconds = user.getRole() == UserRole.CUSTOMER ? 12*60*60 : 4*60*60;
        var token = jwtProvider.generateToken(user.getId(), user.getRole(), expiresInSeconds);

        return new UserWithTokenDto(user, token);
    }

    @Override
    public UserWithTokenDto register(RegisterRequestDto request) {
        var hashedPassword = passwordEncoder.encode(request.getPassword());

        var createUserRequest = new CreateUserRequestDto();
        createUserRequest.setPhoneNum(request.getPhoneNum());
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setHashedPassword(hashedPassword);
        createUserRequest.setFirstName(request.getFirstName());
        createUserRequest.setLastName(request.getLastName());

        var newUser = userService.createUser(createUserRequest);

        var expiresInSeconds = newUser.getRole() == UserRole.CUSTOMER ? 12*60*60 : 4*60*60;
        var token = jwtProvider.generateToken(newUser.getId(), newUser.getRole(), expiresInSeconds);

        return new UserWithTokenDto(newUser, token);
    }

    @Override
    public UserDto me() {
        var currentUserId = SecurityUtil.getCurrentUserId();
        return userService.getUserById(currentUserId);
    }
}
