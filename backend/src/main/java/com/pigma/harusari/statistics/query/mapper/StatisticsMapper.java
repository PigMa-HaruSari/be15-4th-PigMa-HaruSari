package com.pigma.harusari.statistics.query.mapper;

import com.pigma.harusari.statistics.query.dto.response.StatisticsDailyRateResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsMonthlyRateResponse;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface StatisticsMapper {

    StatisticsDailyRateResponse findStatisticsDailyRate(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    StatisticsMonthlyRateResponse findStatisticsMonthlyRate(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}