package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class ScheduleNotFoundException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public ScheduleNotFoundException(TaskErrorCode taskErrorCode) {
        super(taskErrorCode.getMessage());
        this.taskErrorCode = taskErrorCode;
    }
}