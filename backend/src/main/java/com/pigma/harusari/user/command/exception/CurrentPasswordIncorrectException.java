package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class CurrentPasswordIncorrectException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public CurrentPasswordIncorrectException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}