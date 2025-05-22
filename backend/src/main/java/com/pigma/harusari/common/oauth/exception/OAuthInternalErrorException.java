package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthInternalErrorException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthInternalErrorException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}