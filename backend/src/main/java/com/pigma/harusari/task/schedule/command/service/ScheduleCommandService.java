package com.pigma.harusari.task.schedule.command.service;

import com.pigma.harusari.task.schedule.command.dto.request.ScheduleCreateRequest;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleUpdateRequest;
import com.pigma.harusari.task.schedule.command.dto.response.ScheduleCommandResponse;

import java.time.LocalDate;

public interface ScheduleCommandService {

    void createSchedule(ScheduleCreateRequest scheduleCreateRequest);

    Long createSchedule(ScheduleCreateRequest request, Long memberId);

    void updateSchedule(Long scheduleId, ScheduleUpdateRequest request, Long memberId);

    void deleteSchedule(Long scheduleId, Long memberId);

    ScheduleCommandResponse updateCompletionStatus(Long scheduleId, Boolean completionStatus, Long memberId);

    void deleteSchedulesAfter(Long automationScheduleId, LocalDate baseDate);
}