package com.pigma.harusari.task.schedule.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleDto {
    private final Long scheduleId;
    private final Long automationScheduleId;
    private final Long categoryId;
    private final String categoryName;
    private final String scheduleContent;
    private final LocalDate scheduleDate;
    private final Boolean completionStatus;

    @Builder
    public ScheduleDto(
            Long scheduleId, Long automationScheduleId, Long categoryId, String categoryName,
            String scheduleContent, LocalDate scheduleDate, Boolean completionStatus
    ) {
        this.scheduleId = scheduleId;
        this.automationScheduleId = automationScheduleId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate;
        this.completionStatus = completionStatus;
    }

}