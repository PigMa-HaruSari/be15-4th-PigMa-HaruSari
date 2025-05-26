package com.pigma.harusari.diary.exception;

import lombok.Getter;

@Getter
public class DiaryException extends RuntimeException {

    private final DiaryErrorCode errorCode;

    public DiaryException(DiaryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
