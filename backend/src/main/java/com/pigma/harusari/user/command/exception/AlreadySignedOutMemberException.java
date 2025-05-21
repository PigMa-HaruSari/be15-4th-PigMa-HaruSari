package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class AlreadySignedOutMemberException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public AlreadySignedOutMemberException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}