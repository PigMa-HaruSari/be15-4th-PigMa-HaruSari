package com.pigma.harusari.task.schedule.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleDto {
    private Long scheduleId;
    private Long automationScheduleId;
    private Long categoryId;
    private String categoryName;
    private String scheduleContent;
    private LocalDate scheduleDate;
    private Boolean completionStatus;

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