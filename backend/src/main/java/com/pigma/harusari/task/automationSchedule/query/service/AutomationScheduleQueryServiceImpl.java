package com.pigma.harusari.task.automationSchedule.query.service;

import com.pigma.harusari.task.automationSchedule.query.dto.request.AutomationScheduleRequest;
import com.pigma.harusari.task.automationSchedule.query.dto.response.AutomationScheduleDto;
import com.pigma.harusari.task.automationSchedule.query.mapper.AutomationScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutomationScheduleQueryServiceImpl implements AutomationScheduleQueryService {

    private final AutomationScheduleMapper automationScheduleMapper;

    @Transactional(readOnly = true)
    public List<AutomationScheduleDto> getAutomationScheduleList(AutomationScheduleRequest request, Long memberId) {
        return automationScheduleMapper.selectAutomationScheduleList(request, memberId);
    }

}
