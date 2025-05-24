package com.pigma.harusari.task.schedule.query.service;

import com.pigma.harusari.task.schedule.query.dto.response.ScheduleListResponse;

import java.time.LocalDate;

public interface ScheduleQueryService {

    ScheduleListResponse getScheduleList(Long categoryId, LocalDate scheduleDate, Long memberId);
}
