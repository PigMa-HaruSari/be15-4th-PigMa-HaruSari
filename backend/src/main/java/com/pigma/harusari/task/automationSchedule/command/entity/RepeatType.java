package com.pigma.harusari.task.automationSchedule.command.entity;

import java.util.Arrays;

public enum RepeatType {
    DAILY, WEEKLY, MONTHLY;

    public static RepeatType from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("[ERROR] RepeatType 값이 null입니다.");
        }
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 RepeatType 값: " + value));
    }
}
