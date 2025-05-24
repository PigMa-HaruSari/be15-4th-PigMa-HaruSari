package com.pigma.harusari.task.automationSchedule.command.entity;

import com.pigma.harusari.task.exception.InvalidRepeatTypeException;
import com.pigma.harusari.task.exception.TaskErrorCode;

import java.util.Arrays;

public enum RepeatType {
    DAILY, WEEKLY, MONTHLY;

    public static RepeatType from(String value) {
        if (value == null) {
            throw new InvalidRepeatTypeException(TaskErrorCode.REPEAT_TYPE_IS_NULL);
        }
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new InvalidRepeatTypeException(TaskErrorCode.REPEAT_TYPE_NOT_ALLOWED));
    }
}
