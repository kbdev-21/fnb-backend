package com.example.fnb.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DomainExceptionCode {
    /* user-auth-security */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    USER_IS_UNVERIFIED(HttpStatus.CONFLICT),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    WRONG_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    USER_NOT_ALLOWED(HttpStatus.FORBIDDEN),

    /* products */
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_PRICE(HttpStatus.CONFLICT),

    /* discount */
    DISCOUNT_NOT_EXISTED(HttpStatus.NOT_FOUND),
    INVALID_DISCOUNT_VALUE(HttpStatus.CONFLICT),
    PRICE_IS_TOO_LOW_TO_APPLY(HttpStatus.CONFLICT),
    DISCOUNT_EXPIRED(HttpStatus.CONFLICT),
    USER_NOT_ALLOWED_TO_USE(HttpStatus.CONFLICT),
    DISCOUNT_OUT_OF_USE(HttpStatus.CONFLICT),

    /* store */
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND),
    TABLE_CODE_MUST_INCLUDE_ITS_STORE_CODE(HttpStatus.CONFLICT),;

    private final HttpStatus httpStatus;

    DomainExceptionCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
