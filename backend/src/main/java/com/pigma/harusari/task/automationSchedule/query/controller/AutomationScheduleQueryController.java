package com.pigma.harusari.task.automationSchedule.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.automationSchedule.query.dto.request.AutomationScheduleRequest;
import com.pigma.harusari.task.automationSchedule.query.dto.response.AutomationScheduleDto;
import com.pigma.harusari.task.automationSchedule.query.service.AutomationScheduleQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "자동화 일정 조회 API", description = "자동화 일정 목록과 검색일 이후 가장 가까운 일정을 조회하는 API")
public class AutomationScheduleQueryController {

    private final AutomationScheduleQueryService automationScheduleQueryService;

    @GetMapping("/task/automationschedules")
    @Operation(
            summary = "자동화 일정 목록 조회", description = "자동화 일정 목록을 조회한다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "자동화 일정 목록을 반환한다.")
    public ResponseEntity<List<AutomationScheduleDto>> getAutomationSchedules(
            AutomationScheduleRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        List<AutomationScheduleDto> schedules = automationScheduleQueryService.getAutomationScheduleList(request, memberId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/task/automationschedules/nearest")
    @Operation(
            summary = "가장 가까운 자동화 일정 조회", description = "지정한 자동화 일정 ID를 기준으로, 오늘 이후 가장 가까운 일정을 반환한다."
    )
    @Parameter(name = "automationScheduleId", description = "조회할 자동화 일정 ID", example = "1")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "가장 가까운 자동화 일정 정보 반환")
    public ResponseEntity<ApiResponse<AutomationScheduleDto>> getNearestSchedule(
            @RequestParam("automationScheduleId") Long automationScheduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        AutomationScheduleDto dto = automationScheduleQueryService.getNearestAutomationSchedule(automationScheduleId, memberId);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

}
