package com.pigma.harusari.task.schedule.query.service;

import com.pigma.harusari.task.schedule.query.dto.response.ScheduleDto;
import com.pigma.harusari.task.schedule.query.dto.response.ScheduleListResponse;
import com.pigma.harusari.task.schedule.query.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleMapper scheduleMapper;

    @Transactional(readOnly = true)
    public ScheduleListResponse getScheduleList(Long categoryId, LocalDate scheduleDate, Long memberId) {
        // scheduleDate를 파라미터로 추가
        List<ScheduleDto> schedules = scheduleMapper.selectScheduleByMemberId(categoryId, scheduleDate, memberId);
        return ScheduleListResponse.builder()
                .schedule(schedules)
                .build();
    }

}