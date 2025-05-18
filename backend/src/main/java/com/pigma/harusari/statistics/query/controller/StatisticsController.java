package com.pigma.harusari.statistics.query.controller;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsCategoryResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsDayResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsMonthResponse;
import com.pigma.harusari.statistics.query.service.StatisticsService;
import com.pigma.harusari.statistics.query.utils.StatisticsRequestParser;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StatisticsController {

    private static final long ONE_DAY = 1L;

    private final StatisticsService statisticsService;

    @GetMapping("/statistics/daily")
    public ResponseEntity<ApiResponse<StatisticsDayResponse>> getStatisticsDaily(
            @RequestParam(value = "date") @Nullable String date
    ) {
        LocalDate parseDate = StatisticsRequestParser.parseDate(date);

        Long memberId = 1L; // 스프링 시큐리티 구현 완료되면 변경할 예정

        LocalDateTime startDateTime = parseDate.atStartOfDay();
        LocalDateTime endDateTime = parseDate.plusDays(ONE_DAY).atStartOfDay();

        StatisticsDayResponse statisticsDaily = statisticsService.getStatisticsDaily(memberId, startDateTime, endDateTime);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(statisticsDaily));
    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<ApiResponse<StatisticsMonthResponse>> getStatisticsMonthly(
            @RequestParam(value = "date") @Nullable String date
    ) {
        LocalDate parseDate = StatisticsRequestParser.parseDate(date);

        Long memberId = 1L; // 스프링 시큐리티 구현 완료되면 변경할 예정

        LocalDateTime startDateTime = parseDate.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDateTime = parseDate.plusMonths(1).withDayOfMonth(1).atStartOfDay();

        StatisticsMonthResponse statisticsMonthly = statisticsService.getStatisticsMonthly(memberId, startDateTime, endDateTime);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(statisticsMonthly));
    }

    @GetMapping("/statistics/category")
    public ResponseEntity<ApiResponse<StatisticsCategoryResponse>> getStatisticsCategory() {
        Long memberId = 1L; // 스프링 시큐리티 구현 완료되면 변경할 예정

        StatisticsCategoryResponse statisticsCategory = statisticsService.getStatisticsCategory(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(statisticsCategory));
    }

}