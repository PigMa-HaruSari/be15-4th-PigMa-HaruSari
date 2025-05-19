package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class EmailDuplicatedException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public EmailDuplicatedException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}