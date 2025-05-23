package com.pigma.harusari.task.automationSchedule.command.service;

import com.pigma.harusari.task.automationSchedule.command.dto.request.AutomationScheduleCreateRequest;

public interface AutomationScheduleService {

    Long createAutomationSchedule(AutomationScheduleCreateRequest request, Long memberId);

    void updateAutomationSchedule(Long scheduleId, AutomationScheduleCreateRequest request, Long memberId);

    void deleteSchedulesAfter(Long automationScheduleId, Long memberId);

}
