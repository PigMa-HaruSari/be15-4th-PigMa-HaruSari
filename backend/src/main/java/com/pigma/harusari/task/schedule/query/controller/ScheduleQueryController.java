package com.pigma.harusari.task.schedule.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.exception.InvalidRepeatTypeException;
import com.pigma.harusari.task.exception.NeedLoginException;
import com.pigma.harusari.task.exception.TaskErrorCode;
import com.pigma.harusari.task.schedule.query.dto.response.ScheduleListResponse;
import com.pigma.harusari.task.schedule.query.service.ScheduleQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "일정 조회 API", description = "카테고리, 특정 날짜로 일정을 조회하는 API")

public class ScheduleQueryController {

    private final ScheduleQueryService scheduleQueryService;

    @GetMapping("/task/schedule")
    @Operation(summary = "일정 목록 조회", description = "카테고리 ID와 일정 날짜(옵션)를 입력받아 해당 조건에 맞는 일정 목록을 조회한다.")
    @Parameters({
            @Parameter(name = "categoryId", description = "조회할 카테고리 ID (선택)", example = "1"),
            @Parameter(name = "scheduleDate", description = "조회할 일정 날짜 (yyyy-MM-dd, 선택)", example = "2025-06-01")
    })
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조건에 맞는 일정 목록을 반환합니다.")
    public ResponseEntity<ApiResponse<ScheduleListResponse>> getScheduleList(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "scheduleDate", required = false) LocalDate scheduleDate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new NeedLoginException(TaskErrorCode.NEED_LOGIN);
        }
        Long memberId = userDetails.getMemberId();
        if (memberId == null) {
            throw new InvalidRepeatTypeException(TaskErrorCode.INVALID_MEMBER_INFO);
        }

        ScheduleListResponse response = scheduleQueryService.getScheduleList(categoryId, scheduleDate, memberId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
