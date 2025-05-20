package com.pigma.harusari.statistics.query.service;

import com.pigma.harusari.statistics.common.StatisticsType;
import com.pigma.harusari.statistics.query.dto.response.*;
import com.pigma.harusari.statistics.query.mapper.StatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("[통계 - service] StatisticsServiceImpl 테스트")
class StatisticsServiceImplTest {

    @InjectMocks
    StatisticsServiceImpl statisticsServiceImpl;

    @Mock
    StatisticsMapper statisticsMapper;

    StatisticsDayResponse statisticsDayResponse;
    StatisticsMonthResponse statisticsMonthResponse;
    StatisticsCategoryResponse statisticsCategoryResponse;

    StatisticsDailyRateResponse statisticsDailyRateResponse;
    StatisticsMonthlyRateResponse statisticsMonthlyRateResponse;
    List<StatisticsCategoryRateResponse> statisticsCategoryRateList;

    @BeforeEach
    void setUp() {
        statisticsDailyRateResponse = StatisticsDailyRateResponse.builder()
                .achievementRate(55.25)
                .build();

        statisticsMonthlyRateResponse = StatisticsMonthlyRateResponse.builder()
                .achievementRate(75.00)
                .build();

        statisticsCategoryRateList = List.of(
                StatisticsCategoryRateResponse.builder()
                        .categoryName("코딩")
                        .color("#000000")
                        .achievementRate(80.55)
                        .build(),
                StatisticsCategoryRateResponse.builder()
                        .categoryName("운동")
                        .color("#FF0000")
                        .achievementRate(65.15)
                        .build()
        );

        statisticsDayResponse = StatisticsDayResponse.builder()
                .type(StatisticsType.DAILY.name())
                .date(LocalDate.now())
                .achievementRate(statisticsDailyRateResponse.achievementRate())
                .build();

        statisticsMonthResponse = StatisticsMonthResponse.builder()
                .type(StatisticsType.MONTHLY.name())
                .date(LocalDate.now())
                .achievementRate(statisticsMonthlyRateResponse.achievementRate())
                .build();

        statisticsCategoryResponse = StatisticsCategoryResponse.builder()
                .type(StatisticsType.CATEGORY.name())
                .categoryRates(statisticsCategoryRateList)
                .build();
    }

    @Test
    @DisplayName("[일일 달성률] 완료한 개수와 미완료한 개수를 구하는 테스트")
    void testGetStatisticsDaily() {
        Long memberId = 1L;
        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        when(statisticsMapper.findStatisticsDailyRate(memberId, startDateTime, endDateTime)).thenReturn(statisticsDailyRateResponse);

        StatisticsDayResponse statisticsDaily = statisticsServiceImpl.getStatisticsDaily(memberId, startDateTime, endDateTime);

        log.info("statisticsDaily = {}", statisticsDaily);

        assertThat(statisticsDaily).isEqualTo(statisticsDayResponse);
        assertThat(statisticsDaily.type()).isEqualTo(statisticsDayResponse.type());
        assertThat(statisticsDaily.date()).isEqualTo(statisticsDayResponse.date());
        assertThat(statisticsDaily.achievementRate()).isEqualTo(statisticsDayResponse.achievementRate());
    }

    @Test
    @DisplayName("[일일 달성률] 할 일을 작성하지 않는 경우 완료한 개수와 미완료한 개수를 구하는 테스트")
    void testDailyStatisticsWithEmptyTaskList() {
        Long memberId = 1L;
        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        when(statisticsMapper.findStatisticsDailyRate(memberId, startDateTime, endDateTime)).thenReturn(null);

        StatisticsDayResponse statisticsDaily = statisticsServiceImpl.getStatisticsDaily(memberId, startDateTime, endDateTime);

        log.info("statisticsDaily = {}", statisticsDaily);

        assertThat(statisticsDaily).isNotNull();
        assertThat(statisticsDaily.achievementRate()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("[월별 달성률] 완료한 개수와 미완료한 개수를 구하는 테스트")
    void testGetStatisticsMonthly() {
        Long memberId = 1L;
        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusMonths(1).atStartOfDay();

        when(statisticsMapper.findStatisticsMonthlyRate(memberId, startDateTime, endDateTime)).thenReturn(statisticsMonthlyRateResponse);

        StatisticsMonthResponse statisticsMonthly = statisticsServiceImpl.getStatisticsMonthly(memberId, startDateTime, endDateTime);

        log.info("statisticsMonthly = {}", statisticsMonthly);

        assertThat(statisticsMonthly).isEqualTo(statisticsMonthResponse);
        assertThat(statisticsMonthly.type()).isEqualTo(statisticsMonthResponse.type());
        assertThat(statisticsMonthly.date()).isEqualTo(statisticsMonthResponse.date());
        assertThat(statisticsMonthly.achievementRate()).isEqualTo(statisticsMonthResponse.achievementRate());
    }

    @Test
    @DisplayName("[월별 달성률] 할 일을 작성하지 않는 경우 완료한 개수와 미완료한 개수를 구하는 테스트")
    void testMonthlyStatisticsWithEmptyTaskList() {
        Long memberId = 1L;
        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        when(statisticsMapper.findStatisticsMonthlyRate(memberId, startDateTime, endDateTime)).thenReturn(null);

        StatisticsMonthResponse statisticsMonthly = statisticsServiceImpl.getStatisticsMonthly(memberId, startDateTime, endDateTime);

        log.info("statisticsMonthly = {}", statisticsMonthly);

        assertThat(statisticsMonthly).isNotNull();
        assertThat(statisticsMonthly.achievementRate()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("[카테고리 달성률] 완료한 개수와 미완료한 개수를 구하는 테스트")
    void testGetStatisticsCategory() {
        Long memberId = 1L;

        when(statisticsMapper.findStatisticsCategoryRate(memberId)).thenReturn(statisticsCategoryRateList);

        StatisticsCategoryResponse statisticsCategory = statisticsServiceImpl.getStatisticsCategory(memberId);

        log.info("statisticsCategory = {}", statisticsCategory);

        assertThat(statisticsCategory).isEqualTo(statisticsCategoryResponse);
        assertThat(statisticsCategory.type()).isEqualTo(statisticsCategoryResponse.type());
        assertThat(statisticsCategory.categoryRates().get(0).categoryName()).isEqualTo(statisticsCategoryRateList.get(0).categoryName());
        assertThat(statisticsCategory.categoryRates().get(0).color()).isEqualTo(statisticsCategoryRateList.get(0).color());
        assertThat(statisticsCategory.categoryRates().get(0).achievementRate()).isEqualTo(statisticsCategoryRateList.get(0).achievementRate());
        assertThat(statisticsCategory.categoryRates().get(1).categoryName()).isEqualTo(statisticsCategoryRateList.get(1).categoryName());
        assertThat(statisticsCategory.categoryRates().get(1).color()).isEqualTo(statisticsCategoryRateList.get(1).color());
        assertThat(statisticsCategory.categoryRates().get(1).achievementRate()).isEqualTo(statisticsCategoryRateList.get(1).achievementRate());
    }

    @Test
    @DisplayName("[카테고리 달성률] 할 일을 작성하지 않는 경우 완료한 개수와 미완료한 개수를 구하는 테스트")
    void testCategoryStatisticsWithEmptyTaskList() {
        Long memberId = 1L;

        when(statisticsMapper.findStatisticsCategoryRate(memberId)).thenReturn(null);

        StatisticsCategoryResponse statisticsCategory = statisticsServiceImpl.getStatisticsCategory(memberId);

        log.info("statisticsCategory = {}", statisticsCategory);

        assertThat(statisticsCategory).isNotNull();
        assertThat(statisticsCategory.categoryRates().get(0).categoryName()).isEqualTo(null);
        assertThat(statisticsCategory.categoryRates().get(0).achievementRate()).isEqualTo(0.0);
    }

}