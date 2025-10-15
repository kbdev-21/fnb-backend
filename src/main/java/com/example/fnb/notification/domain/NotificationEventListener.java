package com.example.fnb.notification.domain;

import com.example.fnb.auth.RegisterSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

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
            "Chào mừng đến với thương hiệu của chúng tôi",
            "Bạn vừa đăng ký tài khoản thành công tại thương hiệu của chúng tôi. Đây là mã giảm giá: ABCXYZ123"
        );
        System.out.println("User register: " + event.getUser().getEmail() + " at " + Instant.ofEpochMilli(event.getTimestamp()));
    }
}
