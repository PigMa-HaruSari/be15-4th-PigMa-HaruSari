package com.pigma.harusari.user.command.exception;

import lombok.Getter;

@Getter
public class CategoryRequiredException extends RuntimeException {

    private final UserCommandErrorCode errorCode;

    public CategoryRequiredException(UserCommandErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}