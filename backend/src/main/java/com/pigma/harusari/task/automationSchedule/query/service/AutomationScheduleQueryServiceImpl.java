package com.pigma.harusari.task.automationSchedule.query.service;

import com.pigma.harusari.task.automationSchedule.query.dto.request.AutomationScheduleRequest;
import com.pigma.harusari.task.automationSchedule.query.dto.response.AutomationScheduleDto;
import com.pigma.harusari.task.automationSchedule.query.mapper.AutomationScheduleMapper;
import com.pigma.harusari.task.exception.ScheduleNotFoundException;
import com.pigma.harusari.task.exception.TaskErrorCode;
import com.pigma.harusari.task.schedule.command.entity.Schedule;
import com.pigma.harusari.task.schedule.command.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutomationScheduleQueryServiceImpl implements AutomationScheduleQueryService {

    private final AutomationScheduleMapper automationScheduleMapper;
    private final ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public List<AutomationScheduleDto> getAutomationScheduleList(AutomationScheduleRequest request, Long memberId) {
        return automationScheduleMapper.selectAutomationScheduleList(request, memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public AutomationScheduleDto getNearestAutomationSchedule(Long automationScheduleId, Long memberId) {
        LocalDate baseDate = LocalDate.now();

        Schedule nearest = scheduleRepository
                .findFirstByAutomationSchedule_AutomationScheduleIdAndScheduleDateGreaterThanEqualOrderByScheduleDateAsc(
                        automationScheduleId, baseDate
                )
                .orElseThrow(() -> new ScheduleNotFoundException(TaskErrorCode.SCHEDULE_NOT_FOUND));

        return AutomationScheduleDto.builder()
                .automationScheduleId(nearest.getAutomationSchedule().getAutomationScheduleId())
                .automationScheduleContent(nearest.getAutomationSchedule().getAutomationScheduleContent())
                .scheduleDate(nearest.getScheduleDate())
                .repeatType(nearest.getAutomationSchedule().getRepeatType().name())
                .categoryId(nearest.getAutomationSchedule().getCategoryId())
                .categoryName(nearest.getCategory().getCategoryName())
                .build();
    }

}
