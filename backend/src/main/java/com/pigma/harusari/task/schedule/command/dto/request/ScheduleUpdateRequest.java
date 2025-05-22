package com.pigma.harusari.task.schedule.command.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ScheduleUpdateRequest {

    @NotNull
    private final Long categoryId;

    @NotBlank
    private final String scheduleContent;

    @NotNull
    private final LocalDate scheduleDate;
}