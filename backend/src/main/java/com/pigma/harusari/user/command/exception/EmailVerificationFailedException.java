package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class EmailVerificationFailedException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public EmailVerificationFailedException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}