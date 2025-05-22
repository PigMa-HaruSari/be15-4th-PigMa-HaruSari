package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthInvalidCodeException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthInvalidCodeException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}