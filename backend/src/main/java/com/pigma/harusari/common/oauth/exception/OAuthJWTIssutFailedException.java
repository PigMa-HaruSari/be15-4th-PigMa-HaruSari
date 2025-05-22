package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthJWTIssutFailedException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthJWTIssutFailedException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}