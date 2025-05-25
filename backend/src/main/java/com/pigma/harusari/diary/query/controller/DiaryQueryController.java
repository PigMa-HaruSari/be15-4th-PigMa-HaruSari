package com.pigma.harusari.diary.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.diary.query.dto.DiaryResponseDto;
import com.pigma.harusari.diary.query.service.DiaryQueryServiceImpl;
import com.pigma.harusari.statistics.query.utils.StatisticsRequestParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/diary")
@RequiredArgsConstructor
@Tag(name = "회고 조회 API", description = "특정 날짜의 회고 조회 API")
public class DiaryQueryController {

    private final DiaryQueryServiceImpl diaryQueryService;

    @GetMapping
    @Operation(
            summary = "회고 조회",
            description = "사용자가 작성한 특정 날짜의 회고를 조회합니다. 날짜를 입력하지 않으면 오늘 날짜로 조회합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "회고 조회 성공"
                    )
            }
    )
    public ResponseEntity<ApiResponse<DiaryResponseDto>> getDiaryByDate(
            @Parameter(description = "조회할 날짜 (예: 2025-05-23)", example = "2025-05-23")
            @RequestParam(value = "date", required = false) @Nullable String date,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LocalDate parseDate = StatisticsRequestParser.parseDate(date);
        Long memberId = userDetails.getMemberId();

        DiaryResponseDto diary = diaryQueryService.getDiaryByDate(memberId, parseDate);
        return ResponseEntity.ok(ApiResponse.success(diary));
    }
}
