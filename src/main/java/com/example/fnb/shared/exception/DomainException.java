package com.example.fnb.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {
  private final DomainExceptionCode code;
  private final HttpStatus httpStatus;

  public DomainException(DomainExceptionCode code) {
    this.code = code;
    this.httpStatus = code.getHttpStatus();
  }
}
