package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class TaskException extends RuntimeException {
    private final TaskErrorCode taskErrorCode;

    public TaskException(TaskErrorCode taskErrorCode) {
        super(taskErrorCode.getMessage());
        this.taskErrorCode = taskErrorCode;

    }

}
