package com.pigma.harusari.alarm.exception;

import lombok.Getter;

@Getter
public class AlarmException extends RuntimeException {

    private final AlarmErrorCode errorCode;

    public AlarmException(AlarmErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
