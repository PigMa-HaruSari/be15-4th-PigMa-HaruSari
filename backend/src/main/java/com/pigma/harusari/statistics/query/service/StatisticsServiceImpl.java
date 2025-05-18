package com.pigma.harusari.statistics.query.service;

import com.pigma.harusari.statistics.common.StatisticsType;
import com.pigma.harusari.statistics.query.dto.response.StatisticsDailyRateResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsDayResponse;
import com.pigma.harusari.statistics.query.mapper.StatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private static final double DEFAULT_ACHIEVEMENT_RATE = 0.0;

    private final StatisticsMapper statisticsMapper;

    @Override
    public StatisticsDayResponse getStatisticsDaily(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        StatisticsDailyRateResponse dayRateResponse = statisticsMapper.findStatisticsDailyRate(memberId, startDateTime, endDateTime);

        if (dayRateResponse == null) {
            return StatisticsDayResponse.builder()
                    .achievementRate(DEFAULT_ACHIEVEMENT_RATE)
                    .build();
        }

        return StatisticsDayResponse.builder()
                .type(StatisticsType.DAILY.name())
                .date(startDateTime.toLocalDate())
                .achievementRate(dayRateResponse.achievementRate())
                .build();
    }

}