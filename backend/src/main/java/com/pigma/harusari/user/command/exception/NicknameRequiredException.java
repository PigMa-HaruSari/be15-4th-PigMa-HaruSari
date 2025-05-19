package com.pigma.harusari.user.command.exception;

import com.pigma.harusari.statistics.query.exception.StatisticsErrorCode;
import lombok.Getter;

@Getter
public class NicknameRequiredException extends RuntimeException {

    private final StatisticsErrorCode errorCode;

    public NicknameRequiredException(StatisticsErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}