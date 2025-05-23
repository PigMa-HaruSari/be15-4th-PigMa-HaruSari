package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class ScheduleAlreadyCompletedException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public ScheduleAlreadyCompletedException(TaskErrorCode taskErrorCode) {
        super(taskErrorCode.getMessage());
        this.taskErrorCode = taskErrorCode;

    }
}