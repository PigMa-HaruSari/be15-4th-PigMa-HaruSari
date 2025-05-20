package com.pigma.harusari.common.auth.exception.handler;

import com.pigma.harusari.common.auth.exception.*;
import com.pigma.harusari.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(LogInMemberNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleLogInMemberNotFoundException(LogInMemberNotFoundException e) {
        AuthErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(LogInPasswordMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleLogInPasswordMismatchException(LogInPasswordMismatchException e) {
        AuthErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleRefreshTokenInvalidException(RefreshTokenInvalidException e) {
        AuthErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(RefreshMemberNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRefreshMemberNotFoundException(RefreshMemberNotFoundException e) {
        AuthErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
