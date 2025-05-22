package com.pigma.harusari.statistics.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsCategoryResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsDayResponse;
import com.pigma.harusari.statistics.query.dto.response.StatisticsMonthResponse;
import com.pigma.harusari.statistics.query.service.StatisticsService;
import com.pigma.harusari.statistics.query.utils.StatisticsRequestParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "통계 API", description = "일일 통계, 월별 통계, 카테고리별 통계 API")
public class StatisticsController {

    private static final long STATISTICS_DAY_RANGE = 1L;
    private static final long STATISTICS_MONTH_RANGE = 1L;
    private static final int FIRST_DAY_OF_MONTH = 1;

    private final StatisticsService statisticsService;

    @GetMapping("/statistics/daily")
    @Operation(
            summary = "일일 통계 조회",
            description = "쿼리 파라미터에 yyyy-MM-dd 형식으로 요청되면 해당 일을 기준으로 타입, 날짜, 완료 퍼센트를 전달한다."
    )
    @Parameter(name = "date", description = "요청할 날짜", example = "2022-01-01")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청한 날짜의 일일 통계 정보를 반환한다.")
    public ResponseEntity<ApiResponse<StatisticsDayResponse>> getStatisticsDaily(
            @RequestParam(value = "date") @Nullable String date,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LocalDate parseDate = StatisticsRequestParser.parseDate(date);

        Long memberId = userDetails.getMemberId();

        LocalDateTime startDateTime = parseDate.atStartOfDay();
        LocalDateTime endDateTime = parseDate.plusDays(STATISTICS_DAY_RANGE).atStartOfDay();

        StatisticsDayResponse statisticsDaily = statisticsService.getStatisticsDaily(memberId, startDateTime, endDateTime);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(statisticsDaily));
    }

    @GetMapping("/statistics/monthly")
    @Operation(
            summary = "월별 통계 조회",
            description = "쿼리 파라미터에 yyyy-MM-dd 형식으로 요청되면 해당 월을 기준으로 타입, 날짜, 완료 퍼센트를 전달한다."
    )
    @Parameter(name = "date", description = "요청할 날짜", example = "2022-01-01")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청한 날짜의 월별 통계 정보를 반환한다.")
    public ResponseEntity<ApiResponse<StatisticsMonthResponse>> getStatisticsMonthly(
            @RequestParam(value = "date") @Nullable String date,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LocalDate parseDate = StatisticsRequestParser.parseDate(date);

        Long memberId = userDetails.getMemberId();

        LocalDateTime startDateTime = parseDate.withDayOfMonth(FIRST_DAY_OF_MONTH).atStartOfDay();
        LocalDateTime endDateTime = parseDate.plusMonths(STATISTICS_MONTH_RANGE).withDayOfMonth(FIRST_DAY_OF_MONTH).atStartOfDay();

        StatisticsMonthResponse statisticsMonthly = statisticsService.getStatisticsMonthly(memberId, startDateTime, endDateTime);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(statisticsMonthly));
    }

    @GetMapping("/statistics/category")
    @Operation(
            summary = "카테고리별 통계 조회",
            description = "회원 개인의 각 카테고리별 타입, 카테고리명, 색상, 완료 퍼센트를 전달한다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리별 통계 정보를 반환한다.")
    public ResponseEntity<ApiResponse<StatisticsCategoryResponse>> getStatisticsCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();

        StatisticsCategoryResponse statisticsCategory = statisticsService.getStatisticsCategory(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(statisticsCategory));
    }

}