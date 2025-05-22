package com.pigma.harusari.task.schedule.query.dto.request;

import lombok.Getter;

@Getter

public class ScheduleSearchRequest {

    private Long scheduleId;

    private Long categoryId;

    private Boolean completionStatus;

    private String categoryName;

    private String dateType;

}
