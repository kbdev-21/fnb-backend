package com.example.fnb.auth.event;

import com.example.fnb.auth.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegisterSuccessEvent extends ApplicationEvent {
    private UserDto user;

    public RegisterSuccessEvent(Object source, UserDto user) {
        super(source);
        this.user = user;
    }
}
