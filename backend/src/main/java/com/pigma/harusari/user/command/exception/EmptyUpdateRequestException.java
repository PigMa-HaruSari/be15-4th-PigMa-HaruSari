package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class EmptyUpdateRequestException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public EmptyUpdateRequestException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}