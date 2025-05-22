package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthAlreadyRegisteredException extends RuntimeException {

    private final OAuthExceptionErrorCode errorCode;

    public OAuthAlreadyRegisteredException(OAuthExceptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}