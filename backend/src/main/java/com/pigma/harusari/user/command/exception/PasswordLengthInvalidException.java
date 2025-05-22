package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class PasswordLengthInvalidException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public PasswordLengthInvalidException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}