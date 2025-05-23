package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class InvalidRepeatTypeException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public InvalidRepeatTypeException(TaskErrorCode taskErrorCode) {
        super(taskErrorCode.getMessage());
        this.taskErrorCode = taskErrorCode;
    }
}