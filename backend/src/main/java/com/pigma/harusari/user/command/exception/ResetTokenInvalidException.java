package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class ResetTokenInvalidException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public ResetTokenInvalidException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}