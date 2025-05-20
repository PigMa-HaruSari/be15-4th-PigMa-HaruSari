package com.pigma.harusari.common.auth.exception;

import lombok.Getter;

@Getter
public class RefreshTokenInvalidException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public RefreshTokenInvalidException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}