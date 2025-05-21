package com.pigma.harusari.user.command.exception.handler;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.command.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserCommandExceptionHandler {

    @ExceptionHandler(EmailVerificationFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailVerificationFailedException(EmailVerificationFailedException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailDuplicatedException(EmailDuplicatedException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(NicknameRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleNicknameRequiredException(NicknameRequiredException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CategoryRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryRequiredException(CategoryRequiredException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(EmptyUpdateRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmptyUpdateRequestException(EmptyUpdateRequestException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CurrentPasswordIncorrectException.class)
    public ResponseEntity<ApiResponse<Void>> handleCurrentPasswordIncorrectException(CurrentPasswordIncorrectException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(NewPasswordMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleNewPasswordMismatchException(NewPasswordMismatchException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(PasswordLengthInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handlePasswordLengthInvalidException(PasswordLengthInvalidException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(AlreadySignedOutMemberException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadySignedOutMemberException(AlreadySignedOutMemberException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailNotFoundException(EmailNotFoundException e) {
        UserCommandErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
