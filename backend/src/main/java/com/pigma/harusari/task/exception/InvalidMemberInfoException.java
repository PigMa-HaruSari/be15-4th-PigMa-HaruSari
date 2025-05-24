package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class InvalidMemberInfoException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public InvalidMemberInfoException() {
        super(TaskErrorCode.INVALID_MEMBER_INFO.getMessage());
        this.taskErrorCode = TaskErrorCode.INVALID_MEMBER_INFO;
    }
}