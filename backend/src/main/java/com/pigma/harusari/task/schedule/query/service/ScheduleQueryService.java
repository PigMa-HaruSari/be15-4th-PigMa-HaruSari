package com.pigma.harusari.task.schedule.query.service;

import com.pigma.harusari.task.schedule.query.dto.response.ScheduleDto;
import com.pigma.harusari.task.schedule.query.dto.response.ScheduleListResponse;
import com.pigma.harusari.task.schedule.query.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleMapper scheduleMapper;

//    @Transactional(readOnly = true)
//    public ScheduleListResponse getScheduleList(ScheduleSearchRequest scheduleSearchRequest) {
//        List<ScheduleDto> schedule = scheduleMapper.selectSchedule(scheduleSearchRequest);
//        return ScheduleListResponse.builder()
//                .schedule(schedule)
//                .build();
//    }

    @Transactional(readOnly = true)
    public ScheduleListResponse getScheduleList(Long categoryId, Long memberId) {
        // memberId를 조건에 포함해서 해당 회원의 일정만 조회
        List<ScheduleDto> schedules = scheduleMapper.selectScheduleByMemberId(categoryId, memberId);
        ScheduleListResponse response = ScheduleListResponse.builder()
                .schedule(schedules)
                .build();
    return response;

    }

}