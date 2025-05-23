package com.pigma.harusari.diary.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.diary.query.dto.DiaryResponseDto;
import com.pigma.harusari.diary.query.service.DiaryQueryServiceImpl;
import com.pigma.harusari.statistics.query.utils.StatisticsRequestParser;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/diary")
@RequiredArgsConstructor
public class DiaryQueryController {

    private final DiaryQueryServiceImpl diaryQueryService;

    @GetMapping
    @Parameter(name = "date", description = "조회할 날짜", example = "2025-05-23")
    public ResponseEntity<ApiResponse<DiaryResponseDto>> getDiaryByDate(
            @RequestParam("date") @Nullable String date,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LocalDate parseDate = StatisticsRequestParser.parseDate(date);
        Long memberId = userDetails.getMemberId();

        DiaryResponseDto diary = diaryQueryService.getDiaryByDate(memberId, parseDate);
        return ResponseEntity.ok(ApiResponse.success(diary));
    }
}
