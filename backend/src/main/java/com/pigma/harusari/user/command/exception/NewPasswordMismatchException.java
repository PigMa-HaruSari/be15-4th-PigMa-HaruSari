package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class NewPasswordMismatchException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public NewPasswordMismatchException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}