package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthTokenRequestFailedException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthTokenRequestFailedException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}