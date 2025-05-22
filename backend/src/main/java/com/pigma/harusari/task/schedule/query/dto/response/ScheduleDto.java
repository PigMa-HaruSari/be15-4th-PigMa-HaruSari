package com.pigma.harusari.task.schedule.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ScheduleDto {
    private Long scheduleId;
    private Long automationScheduleId;
    private Long categoryId;
    private String categoryName;
    private String scheduleContent;
    private LocalDate scheduleDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean completionStatus;

    @Builder
    public ScheduleDto(
            Long scheduleId, Long automationScheduleId, Long categoryId, String categoryName, String scheduleContent,
            LocalDate scheduleDate, LocalDateTime createdAt, LocalDateTime modifiedAt, Boolean completionStatus
    ) {
        this.scheduleId = scheduleId;
        this.automationScheduleId = automationScheduleId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.completionStatus = completionStatus;
    }
}



