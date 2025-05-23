package com.pigma.harusari.task.automationSchedule.query.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AutomationScheduleRequest {

    private final Long categoryId;

    private String repeatType;

    private final Long automationScheduleId;

    private String automationScheduleContent;

    private final String categoryName;

    @Builder
    public AutomationScheduleRequest(
            Long categoryId, String repeatType, Long automationScheduleId,
            String automationScheduleContent, String categoryName
    ) {
        this.categoryId = categoryId;
        this.repeatType = repeatType;
        this.automationScheduleId = automationScheduleId;
        this.automationScheduleContent = automationScheduleContent;
        this.categoryName = categoryName;
    }


}