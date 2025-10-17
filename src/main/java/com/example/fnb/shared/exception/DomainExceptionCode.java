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
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_PRICE(HttpStatus.CONFLICT),

    /* discount */
    DISCOUNT_NOT_EXISTED(HttpStatus.NOT_FOUND),
    INVALID_DISCOUNT_VALUE(HttpStatus.CONFLICT),
    PRICE_IS_TOO_LOW_TO_APPLY(HttpStatus.CONFLICT),
    DISCOUNT_EXPIRED(HttpStatus.CONFLICT),
    DISCOUNT_OUT_OF_USED(HttpStatus.CONFLICT),;

    private final HttpStatus httpStatus;

    DomainExceptionCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
