package com.pigma.harusari.task.schedule.command.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
public class ScheduleCommandResponse {


    private Long scheduleId;

    private Long automationScheduleId;

    private Long categoryId;

    private String scheduleContent;

    private LocalDate scheduleDate;

    private Boolean completionStatus;

}
