package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthUserNotFoundException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthUserNotFoundException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}