package com.pigma.harusari.category.exception.handler;

import com.pigma.harusari.category.exception.CategoryException;
import com.pigma.harusari.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.pigma.harusari.category") // category 하위 패키지 전용
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryException(CategoryException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.failure(
                        e.getErrorCode().getErrorCode(),
                        e.getErrorCode().getErrorMessage()
                ));
    }
}
