package com.example.fnb.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DomainExceptionCode {
    /* user */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),

    /* auth-security */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    WRONG_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    NOT_ALLOWED(HttpStatus.FORBIDDEN),

    /* products */
    NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;

    DomainExceptionCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
