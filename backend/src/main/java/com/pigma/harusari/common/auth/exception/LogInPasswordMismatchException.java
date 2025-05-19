package com.pigma.harusari.common.auth.exception;

import lombok.Getter;

@Getter
public class LogInPasswordMismatchException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public LogInPasswordMismatchException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}