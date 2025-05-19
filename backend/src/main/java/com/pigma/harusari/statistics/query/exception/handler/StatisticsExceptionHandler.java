package com.pigma.harusari.statistics.query.exception.handler;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.statistics.query.exception.InvalidDateFormatException;
import com.pigma.harusari.statistics.query.exception.MissingDateException;
import com.pigma.harusari.statistics.query.exception.StatisticsErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StatisticsExceptionHandler {

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDateFormatException(InvalidDateFormatException e) {
        StatisticsErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(MissingDateException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingDateException(MissingDateException e) {
        StatisticsErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}