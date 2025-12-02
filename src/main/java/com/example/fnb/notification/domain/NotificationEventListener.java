package com.example.fnb.notification.domain;

import com.example.fnb.auth.event.RegisterSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class NotificationEventListener {
    private final EmailSender emailSender;

    public NotificationEventListener(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @EventListener
    @Async
    public void handleRegisterSuccessEvent(RegisterSuccessEvent event) {
        emailSender.sendEmailAsync(
            event.getUser().getEmail(),
            "Welcome to our stores",
            "U have created your account successfully!"
        );
        System.out.println("User register: " + event.getUser().getEmail() + " at " + Instant.ofEpochMilli(event.getTimestamp()));
    }
}
