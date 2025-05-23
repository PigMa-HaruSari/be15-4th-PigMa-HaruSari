package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class NeedLoginException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public NeedLoginException(TaskErrorCode errorCode) {
        super(errorCode.getMessage());
        this.taskErrorCode = errorCode;
    }
}