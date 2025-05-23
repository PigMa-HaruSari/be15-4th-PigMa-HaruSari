package com.pigma.harusari.feedback.exception.handler;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.feedback.exception.FeedbackException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeedbackExceptionHandler {

    @ExceptionHandler(FeedbackException.class)
    public ResponseEntity<ApiResponse<Void>> handleFeedbackException(FeedbackException e) {
        return new ResponseEntity<>(
                ApiResponse.failure(
                        e.getErrorCode().getErrorCode(),
                        e.getErrorCode().getErrorMessage()
                ),
                e.getErrorCode().getHttpStatus()
        );
    }
}
