package com.pigma.harusari.diary.exception.handler;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.diary.exception.DiaryException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DiaryExceptionHandler {

    @ExceptionHandler(DiaryException.class)
    public ResponseEntity<ApiResponse<Void>> handleDiaryException(DiaryException e) {
        return new ResponseEntity<>(
                ApiResponse.failure(
                        e.getErrorCode().getErrorCode(),
                        e.getErrorCode().getErrorMessage()
                ),
                e.getErrorCode().getHttpStatus()
        );
    }
}
