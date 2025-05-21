package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthUserInfoIncompleteException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthUserInfoIncompleteException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}