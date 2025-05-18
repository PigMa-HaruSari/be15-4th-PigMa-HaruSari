package com.pigma.harusari.statistics.query.exception;

import lombok.Getter;

@Getter
public class InvalidDateFormatException extends RuntimeException {

    private final StatisticsErrorCode errorCode;

    public InvalidDateFormatException(StatisticsErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}