package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthUserInfoRequestFailedException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthUserInfoRequestFailedException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}