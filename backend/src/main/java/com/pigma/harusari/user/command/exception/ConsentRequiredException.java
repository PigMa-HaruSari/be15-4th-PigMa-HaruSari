package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class ConsentRequiredException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public ConsentRequiredException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}