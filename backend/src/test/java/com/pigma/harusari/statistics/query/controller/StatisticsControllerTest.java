package com.pigma.harusari.statistics.query.controller;

import com.pigma.harusari.statistics.common.StatisticsType;
import com.pigma.harusari.statistics.query.dto.response.StatisticsCategoryRateResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsCategoryResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsDayResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsMonthResponse;
import com.pigma.harusari.statistics.query.exception.StatisticsErrorCode;
import com.pigma.harusari.statistics.query.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(StatisticsController.class)
@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@DisplayName("[통계 - controller] StatisticsController 테스트")
class StatisticsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StatisticsService statisticsService;

    StatisticsDayResponse statisticsDayResponse;
    StatisticsMonthResponse statisticsMonthResponse;
    StatisticsCategoryResponse statisticsCategory;

    @BeforeEach
    void setUp() {
        statisticsDayResponse = StatisticsDayResponse.builder()
                .type(StatisticsType.DAILY.name())
                .date(LocalDate.now())
                .achievementRate(55.25)
                .build();

        statisticsMonthResponse = StatisticsMonthResponse.builder()
                .type(StatisticsType.MONTHLY.name())
                .date(LocalDate.now())
                .achievementRate(75.00)
                .build();

        statisticsCategory = StatisticsCategoryResponse.builder()
                .type(StatisticsType.CATEGORY.name())
                .categoryRates(List.of(StatisticsCategoryRateResponse.builder()
                        .categoryName("코딩")
                        .color("#FF0000")
                        .achievementRate(80.55)
                        .build()))
                .build();
    }

    /* 일일 달성률 */
    @Test
    @DisplayName("[일일 달성률] 통계 조회 테스트")
    void testGetStatisticsDaily() throws Exception {
        Long memberId = 1L;

        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        when(statisticsService.getStatisticsDaily(memberId, startDateTime, endDateTime)).thenReturn(statisticsDayResponse);

        mockMvc.perform(get("/api/v1/statistics/daily")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.type").exists())
                .andExpect(jsonPath("$.data.type").value(StatisticsType.DAILY.name()))
                .andExpect(jsonPath("$.data.date").exists())
                .andExpect(jsonPath("$.data.date").value(date.toString()))
                .andExpect(jsonPath("$.data.achievementRate").value(statisticsDayResponse.achievementRate()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    @DisplayName("[일일 달성률] 통계 조회 시 날짜 값이 경우 예외가 발생하는 테스트")
    void testGetStatisticsDailyDateMissingDateException(String condition) throws Exception {
        mockMvc.perform(get("/api/v1/statistics/daily")
                        .param("date", condition)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(StatisticsErrorCode.MISSING_DATE.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(StatisticsErrorCode.MISSING_DATE.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @ParameterizedTest
    @CsvSource({"2025.01.01", "2025-01-01 15:30:15"})
    @DisplayName("[일일 달성률] 통계 조회 시 날짜 형식이 맞지 않는 경우 예외가 발생하는 테스트")
    void testGetStatisticsDailyInvalidDateFormatException(String condition) throws Exception {
        mockMvc.perform(get("/api/v1/statistics/daily")
                        .param("date", condition)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(StatisticsErrorCode.INVALID_DATE_FORMAT.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(StatisticsErrorCode.INVALID_DATE_FORMAT.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    /* 월별 달성률 */
    @Test
    @DisplayName("[월별 달성률] 통계 조회 테스트")
    void testGetStatisticsMonthly() throws Exception {
        Long memberId = 1L;

        LocalDate date = LocalDate.now();
        LocalDateTime startDateTime = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDateTime = date.plusMonths(1).withDayOfMonth(1).atStartOfDay();

        log.info("startDateTime = {}", startDateTime);
        log.info("endDateTime = {}", endDateTime);

        when(statisticsService.getStatisticsMonthly(memberId, startDateTime, endDateTime)).thenReturn(statisticsMonthResponse);

        mockMvc.perform(get("/api/v1/statistics/monthly")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.type").exists())
                .andExpect(jsonPath("$.data.type").value(StatisticsType.MONTHLY.name()))
                .andExpect(jsonPath("$.data.date").exists())
                .andExpect(jsonPath("$.data.date").value(date.toString()))
                .andExpect(jsonPath("$.data.achievementRate").value(statisticsMonthResponse.achievementRate()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    @DisplayName("[월별 달성률] 통계 조회 시 날짜 값이 경우 예외가 발생하는 테스트")
    void testGetStatisticsMonthlyDateMissingDateException(String condition) throws Exception {
        mockMvc.perform(get("/api/v1/statistics/monthly")
                        .param("date", condition)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(StatisticsErrorCode.MISSING_DATE.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(StatisticsErrorCode.MISSING_DATE.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @ParameterizedTest
    @CsvSource({"2025.02.22", "2025-10-01 23:44:11"})
    @DisplayName("[월별 달성률] 통계 조회 시 날짜 형식이 맞지 않는 경우 예외가 발생하는 테스트")
    void testGetStatisticsMonthlyInvalidDateFormatException(String condition) throws Exception {
        mockMvc.perform(get("/api/v1/statistics/monthly")
                        .param("date", condition)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(StatisticsErrorCode.INVALID_DATE_FORMAT.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(StatisticsErrorCode.INVALID_DATE_FORMAT.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    /* 카테고리별 달성률 */
    @Test
    @DisplayName("[카테고리 달성률] 통계 조회 테스트")
    void testGetStatisticsCategory() throws Exception {
        Long memberId = 1L;

        when(statisticsService.getStatisticsCategory(memberId)).thenReturn(statisticsCategory);

        mockMvc.perform(get("/api/v1/statistics/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.type").exists())
                .andExpect(jsonPath("$.data.type").value(StatisticsType.CATEGORY.name()))
                .andExpect(jsonPath("$.data.categoryRates[0].categoryName").exists())
                .andExpect(jsonPath("$.data.categoryRates[0].categoryName").value(statisticsCategory.categoryRates().get(0).categoryName()))
                .andExpect(jsonPath("$.data.categoryRates[0].color").exists())
                .andExpect(jsonPath("$.data.categoryRates[0].color").value(statisticsCategory.categoryRates().get(0).color()))
                .andExpect(jsonPath("$.data.categoryRates[0].achievementRate").exists())
                .andExpect(jsonPath("$.data.categoryRates[0].achievementRate").value(statisticsCategory.categoryRates().get(0).achievementRate()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

}