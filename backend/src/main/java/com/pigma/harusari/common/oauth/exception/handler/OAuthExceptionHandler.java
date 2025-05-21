package com.pigma.harusari.common.oauth.exception.handler;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.common.oauth.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OAuthExceptionHandler {

    @ExceptionHandler(OAuthUserInfoRequestFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuthUserInfoRequestFailedException(OAuthUserInfoRequestFailedException e) {
        OAuthExceptionErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(OAuthTokenRequestFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuthTokenRequestFailedException(OAuthTokenRequestFailedException e) {
        OAuthExceptionErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(OAuthInvalidCodeException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuthInvalidCodeException(OAuthInvalidCodeException e) {
        OAuthExceptionErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(OAuthUserInfoIncompleteException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuthUserInfoIncompleteException(OAuthUserInfoIncompleteException e) {
        OAuthExceptionErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(OAuthAlreadyRegisteredException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuthAlreadyRegisteredException(OAuthAlreadyRegisteredException e) {
        OAuthExceptionErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(OAuthRedisSaveFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuthRedisSaveFailedException(OAuthRedisSaveFailedException e) {
        OAuthExceptionErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(OAuthJWTIssutFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuthJWTIssutFailedException(OAuthJWTIssutFailedException e) {
        OAuthExceptionErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(OAuthInternalErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuthInternalErrorException(OAuthInternalErrorException e) {
        OAuthExceptionErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
