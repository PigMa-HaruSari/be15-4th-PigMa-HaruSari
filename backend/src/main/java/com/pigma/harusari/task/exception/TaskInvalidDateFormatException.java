package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class TaskInvalidDateFormatException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public TaskInvalidDateFormatException(TaskErrorCode taskErrorCode) {
        super(taskErrorCode.getMessage());
        this.taskErrorCode = taskErrorCode;
    }
}