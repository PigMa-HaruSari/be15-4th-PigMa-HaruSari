package com.pigma.harusari.user.command.exception;

import com.pigma.harusari.statistics.query.exception.StatisticsErrorCode;
import lombok.Getter;

@Getter
public class CategoryRequiredException extends RuntimeException {

    private final StatisticsErrorCode errorCode;

    public CategoryRequiredException(StatisticsErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}