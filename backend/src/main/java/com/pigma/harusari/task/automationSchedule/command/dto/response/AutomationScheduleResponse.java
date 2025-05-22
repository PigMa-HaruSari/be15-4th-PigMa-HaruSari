package com.pigma.harusari.task.automationSchedule.command.dto.response;

import com.pigma.harusari.task.automationSchedule.command.entity.RepeatType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AutomationScheduleResponse {

    private Long automationScheduleId;

    private Long categoryId;

    private String automationScheduleContent;

    private LocalDate scheduleDate;

    private LocalDate endDate;

    private RepeatType repeatType;

    private String repeatWeekdays;

    private Integer repeatMonthday;

}