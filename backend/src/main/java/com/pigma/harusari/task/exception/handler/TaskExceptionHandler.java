package com.pigma.harusari.task.exception.handler;

import  com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleScheduleNotFound(ScheduleNotFoundException e) {
        TaskErrorCode code = e.getTaskErrorCode();
        log.error("Schedule not found: {}", code.getMessage());
        ApiResponse<Void> response = ApiResponse.failure(code.getCode(), code.getMessage());
        return new ResponseEntity<>(response, code.getHttpStatus());
    }

    @ExceptionHandler(AutomationScheduleNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAutomationScheduleNotFound(AutomationScheduleNotFoundException e) {
        TaskErrorCode code = e.getTaskErrorCode();
        log.error("Automation schedule not found: {}", code.getMessage());
        ApiResponse<Void> response = ApiResponse.failure(code.getCode(), code.getMessage());
        return new ResponseEntity<>(response, code.getHttpStatus());
    }

    @ExceptionHandler(TaskInvalidDateFormatException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDateFormat(TaskInvalidDateFormatException e) {
        TaskErrorCode code = e.getTaskErrorCode();
        log.error("Invalid date format: {}", code.getMessage());
        ApiResponse<Void> response = ApiResponse.failure(code.getCode(), code.getMessage());
        return new ResponseEntity<>(response, code.getHttpStatus());
    }

    @ExceptionHandler(InvalidRepeatTypeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRepeatType(InvalidRepeatTypeException e) {
        TaskErrorCode code = e.getTaskErrorCode();
        log.error("Invalid repeat type: {}", code.getMessage());
        ApiResponse<Void> response = ApiResponse.failure(code.getCode(), code.getMessage());
        return new ResponseEntity<>(response, code.getHttpStatus());
    }

    @ExceptionHandler(InvalidScheduleDateException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidScheduleDate(InvalidScheduleDateException e) {
        TaskErrorCode code = e.getTaskErrorCode();
        log.error("Invalid schedule date: {}", code.getMessage());
        ApiResponse<Void> response = ApiResponse.failure(code.getCode(), code.getMessage());
        return new ResponseEntity<>(response, code.getHttpStatus());
    }

    @ExceptionHandler(ScheduleAlreadyCompletedException.class)
    public ResponseEntity<ApiResponse<Void>> handleScheduleAlreadyCompleted(ScheduleAlreadyCompletedException e) {
        TaskErrorCode code = e.getTaskErrorCode();
        log.error("Schedule already completed: {}", code.getMessage());
        ApiResponse<Void> response = ApiResponse.failure(code.getCode(), code.getMessage());
        return new ResponseEntity<>(response, code.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : TaskErrorCode.INVALID_REQUEST.getMessage();
        TaskErrorCode errorCode = TaskErrorCode.INVALID_REQUEST;
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), message);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        TaskErrorCode errorCode = TaskErrorCode.ILLEGAL_ARGUMENT;
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), e.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryNotFoundException(CategoryNotFoundException e) {
        TaskErrorCode errorCode = e.getTaskErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


    @ExceptionHandler(NeedLoginException.class)
    public ResponseEntity<ApiResponse<Void>> handleNeedLoginException(NeedLoginException e) {
        TaskErrorCode errorCode = e.getTaskErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(InvalidMemberInfoException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidMemberInfoException(InvalidMemberInfoException e) {
        TaskErrorCode errorCode = e.getTaskErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedModificationException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedModification(UnauthorizedModificationException e) {
        TaskErrorCode code = e.getTaskErrorCode();
        log.error("Unauthorized modification attempt: {}", code.getMessage());
        ApiResponse<Void> response = ApiResponse.failure(code.getCode(), code.getMessage());
        return new ResponseEntity<>(response, code.getHttpStatus());
    }

}
