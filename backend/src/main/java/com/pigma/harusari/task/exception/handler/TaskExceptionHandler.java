package com.pigma.harusari.task.exception.handler;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.exception.TaskErrorCode;
import com.pigma.harusari.task.exception.TaskException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(TaskException e) {
        TaskErrorCode taskErrorCode = e.getTaskErrorCode();
        ApiResponse<Void> response
                = ApiResponse.failure(taskErrorCode.getCode(), taskErrorCode.getMessage());
        return new ResponseEntity<>(response, taskErrorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "잘못된 요청입니다.";
        ApiResponse<Void> response = ApiResponse.failure("20998", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        ApiResponse<Void> response = ApiResponse.failure("20997", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        ApiResponse<Void> response = ApiResponse.failure("20999", "내부 서버 오류입니다.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
