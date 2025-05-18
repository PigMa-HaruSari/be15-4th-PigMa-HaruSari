package com.pigma.harusari.statistics.query.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatisticsErrorCode {

    INVALID_DATE_FORMAT("001", "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식으로 입력해주세요.", HttpStatus.BAD_REQUEST),
    MISSING_DATE("002", "날짜는 필수입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}