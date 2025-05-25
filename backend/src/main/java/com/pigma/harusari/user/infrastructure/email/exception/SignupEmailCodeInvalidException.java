package com.pigma.harusari.user.infrastructure.email.exception;

import com.pigma.harusari.user.command.exception.UserCommandErrorCode;
import lombok.Getter;

@Getter
public class SignupEmailCodeInvalidException extends RuntimeException {

    private final EmailErrorCode errorCode;

    public SignupEmailCodeInvalidException(EmailErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}