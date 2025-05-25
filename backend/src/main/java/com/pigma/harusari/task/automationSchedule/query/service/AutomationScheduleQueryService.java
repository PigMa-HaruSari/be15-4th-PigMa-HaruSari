package com.pigma.harusari.task.automationSchedule.query.service;

import com.pigma.harusari.task.automationSchedule.query.dto.request.AutomationScheduleRequest;
import com.pigma.harusari.task.automationSchedule.query.dto.response.AutomationScheduleDto;

import java.util.List;

public interface AutomationScheduleQueryService {

    List<AutomationScheduleDto> getAutomationScheduleList(AutomationScheduleRequest request, Long memberId);

    AutomationScheduleDto getNearestAutomationSchedule(Long automationScheduleId, Long memberId);
}
