package com.pigma.harusari.user.infrastructure.email.exception.handler;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.command.exception.UserCommandErrorCode;
import com.pigma.harusari.user.infrastructure.email.exception.EmailErrorCode;
import com.pigma.harusari.user.infrastructure.email.exception.ResetEmailCodeInvalidException;
import com.pigma.harusari.user.infrastructure.email.exception.SignupEmailCodeInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class EmailExceptionHandler {

    @ExceptionHandler(SignupEmailCodeInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleSignupEmailCodeInvalidException(SignupEmailCodeInvalidException e) {
        EmailErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ResetEmailCodeInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleResetEmailCodeInvalidException(ResetEmailCodeInvalidException e) {
        EmailErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}