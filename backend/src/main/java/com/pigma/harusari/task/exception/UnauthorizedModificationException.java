package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class UnauthorizedModificationException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public UnauthorizedModificationException(TaskErrorCode taskErrorCode) {
        super(TaskErrorCode.UNAUTHORIZED_MODIFICATION.getMessage());
        this.taskErrorCode = TaskErrorCode.UNAUTHORIZED_MODIFICATION;
    }
}
