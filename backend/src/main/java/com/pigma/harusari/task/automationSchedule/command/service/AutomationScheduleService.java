package com.pigma.harusari.task.automationSchedule.command.service;

import com.pigma.harusari.task.automationSchedule.command.dto.request.AutomationScheduleCreateRequest;
import com.pigma.harusari.task.automationSchedule.command.entity.AutomationSchedule;

import java.time.LocalDate;

public interface AutomationScheduleService {

    Long createAutomationSchedule(AutomationScheduleCreateRequest request, Long memberId);

    void updateAutomationSchedule(Long scheduleId, AutomationScheduleCreateRequest request, Long memberId);

    void deleteSchedulesAfter(Long automationScheduleId, Long memberId);

    void generateSchedulesForAutomation(AutomationSchedule automationSchedule, LocalDate startDate);

    void softDelete(Long automationScheduleId, Long memberId);

}
