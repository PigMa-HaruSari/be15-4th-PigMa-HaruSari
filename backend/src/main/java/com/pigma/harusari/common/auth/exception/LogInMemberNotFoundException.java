package com.pigma.harusari.common.auth.exception;

import lombok.Getter;

@Getter
public class LogInMemberNotFoundException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public LogInMemberNotFoundException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}