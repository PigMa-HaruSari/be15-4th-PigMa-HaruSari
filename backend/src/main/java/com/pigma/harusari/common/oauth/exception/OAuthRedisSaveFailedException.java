package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthRedisSaveFailedException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthRedisSaveFailedException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}