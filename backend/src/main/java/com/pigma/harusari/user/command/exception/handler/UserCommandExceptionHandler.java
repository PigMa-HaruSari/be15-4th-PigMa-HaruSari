package com.pigma.harusari.user.command.exception.handler;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.statistics.query.exception.InvalidDateFormatException;
import com.pigma.harusari.statistics.query.exception.StatisticsErrorCode;
import com.pigma.harusari.user.command.exception.CategoryRequiredException;
import com.pigma.harusari.user.command.exception.EmailDuplicatedException;
import com.pigma.harusari.user.command.exception.EmailVerificationFailedException;
import com.pigma.harusari.user.command.exception.NicknameRequiredException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserCommandExceptionHandler {

    @ExceptionHandler(EmailVerificationFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailVerificationFailedException(EmailVerificationFailedException e) {
        StatisticsErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailDuplicatedException(EmailDuplicatedException e) {
        StatisticsErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(NicknameRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleNicknameRequiredException(NicknameRequiredException e) {
        StatisticsErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CategoryRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryRequiredException(CategoryRequiredException e) {
        StatisticsErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


}
