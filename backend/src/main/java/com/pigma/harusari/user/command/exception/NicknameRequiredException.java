package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class NicknameRequiredException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public NicknameRequiredException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}