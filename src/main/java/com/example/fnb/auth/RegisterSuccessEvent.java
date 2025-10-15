package com.example.fnb.auth;

import com.example.fnb.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

@Getter
@Setter
public class RegisterSuccessEvent extends ApplicationEvent {
    private UserDto user;

    public RegisterSuccessEvent(Object source, UserDto user) {
        super(source);
        this.user = user;
    }
}
