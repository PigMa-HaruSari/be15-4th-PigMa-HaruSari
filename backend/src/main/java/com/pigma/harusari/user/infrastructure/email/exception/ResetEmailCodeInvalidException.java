package com.pigma.harusari.user.infrastructure.email.exception;

import com.pigma.harusari.user.command.exception.UserCommandErrorCode;
import lombok.Getter;

@Getter
public class ResetEmailCodeInvalidException extends RuntimeException {

    private final EmailErrorCode errorCode;

    public ResetEmailCodeInvalidException(EmailErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}