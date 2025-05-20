package com.pigma.harusari.alarm.exception.handler;

import com.pigma.harusari.alarm.exception.AlarmException;
import com.pigma.harusari.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AlarmExceptionHandler {

    @ExceptionHandler(AlarmException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlarmException(AlarmException e) {
        return new ResponseEntity<>(
                ApiResponse.failure(
                        e.getErrorCode().getErrorCode(),
                        e.getErrorCode().getErrorMessage()
                ),
                e.getErrorCode().getHttpStatus()
        );
    }
}
