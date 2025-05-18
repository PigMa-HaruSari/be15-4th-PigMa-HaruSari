package com.pigma.harusari.statistics.query.service;

import com.pigma.harusari.statistics.common.StatisticsType;
import com.pigma.harusari.statistics.query.dto.response.*;
import com.pigma.harusari.statistics.query.mapper.StatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public StatisticsMonthResponse getStatisticsMonthly(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        StatisticsMonthlyRateResponse monthlyRateResponse = statisticsMapper.findStatisticsMonthlyRate(memberId, startDateTime, endDateTime);

        if (monthlyRateResponse == null) {
            return StatisticsMonthResponse.builder()
                    .achievementRate(DEFAULT_ACHIEVEMENT_RATE)
                    .build();
        }

        return StatisticsMonthResponse.builder()
                .type(StatisticsType.MONTHLY.name())
                .date(startDateTime.toLocalDate())
                .achievementRate(monthlyRateResponse.achievementRate())
                .build();
    }

    @Override
    public StatisticsCategoryResponse getStatisticsCategory(Long memberId) {
        List<StatisticsCategoryRateResponse> statisticsCategoryRateList = statisticsMapper.findStatisticsCategoryRate(memberId);

        if (statisticsCategoryRateList == null ) {
            return StatisticsCategoryResponse.builder()
                    .type(StatisticsType.CATEGORY.name())
                    .categoryRates(List.of(StatisticsCategoryRateResponse.builder()
                            .categoryName(null)
                            .achievementRate(DEFAULT_ACHIEVEMENT_RATE)
                            .build()))
                    .build();
        }

        return StatisticsCategoryResponse.builder()
                .type(StatisticsType.CATEGORY.name())
                .categoryRates(statisticsCategoryRateList)
                .build();
    }

}