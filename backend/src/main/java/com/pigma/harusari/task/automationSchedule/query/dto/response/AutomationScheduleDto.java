package com.pigma.harusari.task.automationSchedule.query.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AutomationScheduleDto {

    private Long automationScheduleId;

    private String automationScheduleContent;

    private String repeatType;

    private Long categoryId;

    private String categoryName;


    @Builder
    public AutomationScheduleDto(
            Long automationScheduleId, String automationScheduleContent, String repeatType,
            Long categoryId, String categoryName
    ) {
        this.automationScheduleId = automationScheduleId;
        this.automationScheduleContent = automationScheduleContent;
        this.repeatType = repeatType;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

}

