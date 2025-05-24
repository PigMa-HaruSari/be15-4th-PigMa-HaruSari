package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class AutomationScheduleNotFoundException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public AutomationScheduleNotFoundException(TaskErrorCode taskErrorCode) {
        super(taskErrorCode.getMessage());
        this.taskErrorCode = taskErrorCode;

    }
}