package com.pigma.harusari.task.automationSchedule.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class AutomationScheduleDto {

    private Long automationScheduleId;

    private String automationScheduleContent;

    private String repeatType;

    private Long categoryId;

    private String categoryName;

    private LocalDate scheduleDate;

    private LocalDate endDate;

    private boolean deleted_flag;


    @Builder
    public AutomationScheduleDto(
            Long automationScheduleId, String automationScheduleContent, LocalDate scheduleDate, String repeatType,
            Long categoryId, String categoryName, LocalDate endDate, boolean deleted_flag
    ) {
        this.automationScheduleId = automationScheduleId;
        this.automationScheduleContent = automationScheduleContent;
        this.scheduleDate = scheduleDate;
        this.repeatType = repeatType;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.endDate = endDate;
        this.deleted_flag = deleted_flag;
    }

}

