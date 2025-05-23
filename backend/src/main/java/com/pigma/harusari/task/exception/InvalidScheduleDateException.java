package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class InvalidScheduleDateException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public InvalidScheduleDateException(TaskErrorCode taskErrorCode) {
        super(taskErrorCode.getMessage());
        this.taskErrorCode = taskErrorCode;

    }
}