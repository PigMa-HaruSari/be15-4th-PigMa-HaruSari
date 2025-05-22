package com.pigma.harusari.task.automationSchedule.query.dto.request;

import lombok.Getter;

@Getter
public class AutomationScheduleRequest {

    private Long categoryId;

    private String repeatType;

    private Long automationScheduleId;

    private String automationScheduleContent;

    private String categoryName;



}