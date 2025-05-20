package com.pigma.harusari.common.auth.exception;

import lombok.Getter;

@Getter
public class RefreshMemberNotFoundException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public RefreshMemberNotFoundException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}