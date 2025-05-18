package com.pigma.harusari.statistics.query.exception;

import lombok.Getter;

@Getter
public class MissingDateException extends RuntimeException {

    private final StatisticsErrorCode errorCode;

    public MissingDateException(StatisticsErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}