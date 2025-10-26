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

    /* category */
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND),

    /* collection */
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND),
    EMPTY_VALUE(HttpStatus.NOT_FOUND),

    /* discount */
    DISCOUNT_NOT_EXISTED(HttpStatus.NOT_FOUND),
    INVALID_DISCOUNT_VALUE(HttpStatus.CONFLICT),
    PRICE_IS_TOO_LOW_TO_APPLY(HttpStatus.CONFLICT),
    DISCOUNT_EXPIRED(HttpStatus.CONFLICT),
    USER_NOT_ALLOWED_TO_USE(HttpStatus.CONFLICT),
    DISCOUNT_OUT_OF_USE(HttpStatus.CONFLICT),

    /* store */
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND),
    TABLE_NOT_FOUND(HttpStatus.NOT_FOUND),
    DUPLICATE_TABLE_IN_STORE(HttpStatus.CONFLICT),

    /* order */
    STORE_NOT_READY(HttpStatus.CONFLICT),
    MISSING_REQUIRED_OPTIONS(HttpStatus.CONFLICT),
    PRODUCT_INFO_IS_INVALID(HttpStatus.CONFLICT),
    ADDRESS_IS_INVALID(HttpStatus.CONFLICT),;

    private final HttpStatus httpStatus;

    DomainExceptionCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
