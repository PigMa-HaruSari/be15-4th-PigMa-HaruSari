package com.pigma.harusari.feedback.exception;

import lombok.Getter;

@Getter
public class FeedbackException extends RuntimeException {

    private final FeedbackErrorCode errorCode;

    public FeedbackException(FeedbackErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
