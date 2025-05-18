package com.pigma.harusari.statistics.query.service;

import com.pigma.harusari.statistics.query.dto.response.StatisticsDayResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsMonthResponse;

import java.time.LocalDateTime;

public interface StatisticsService {

    StatisticsDayResponse getStatisticsDaily(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    StatisticsMonthResponse getStatisticsMonthly(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}