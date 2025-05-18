package com.pigma.harusari.statistics.query.service;

import com.pigma.harusari.statistics.query.dto.response.StatisticsDayResponse;
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

    @BeforeEach
    void setUp() {
        statisticsDayResponse = StatisticsDayResponse.builder()
                .achievementRate(55.25)
                .build();
    }

    @Test
    @DisplayName("[일일 달성률] 완료한 개수와 미완료한 개수를 구하는 테스트")
    void testGetStatisticsDaily() {
        Long memberId = 1L;
        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        when(statisticsMapper.findStatisticsDaily(memberId, startDateTime, endDateTime)).thenReturn(statisticsDayResponse);

        StatisticsDayResponse statisticsDaily = statisticsServiceImpl.getStatisticsDaily(memberId, startDateTime, endDateTime);

        log.info("statisticsDaily = {}", statisticsDaily);

        assertThat(statisticsDaily).isEqualTo(statisticsDayResponse);
        assertThat(statisticsDaily.achievementRate()).isEqualTo(statisticsDayResponse.achievementRate());
    }

    @Test
    @DisplayName("[일일 달성률] 할 일을 작성하지 않는 경우 완료한 개수와 미완료한 개수를 구하는 테스트")
    void testDailyStatisticsWithEmptyTaskList() {
        Long memberId = 1L;
        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        when(statisticsMapper.findStatisticsDaily(memberId, startDateTime, endDateTime)).thenReturn(null);

        StatisticsDayResponse statisticsDaily = statisticsServiceImpl.getStatisticsDaily(memberId, startDateTime, endDateTime);

        log.info("statisticsDaily = {}", statisticsDaily);

        assertThat(statisticsDaily).isNotNull();
        assertThat(statisticsDaily.achievementRate()).isEqualTo(0.0);
    }

}