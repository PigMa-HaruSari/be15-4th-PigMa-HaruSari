package com.pigma.harusari.task.automationSchedule.command.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.automationSchedule.command.dto.request.AutomationScheduleCreateRequest;
import com.pigma.harusari.task.automationSchedule.command.service.AutomationScheduleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "자동화 일정 API", description = "자동화 일정 추가, 수정, 삭제 API")
public class AutomationScheduleController {

    private final AutomationScheduleServiceImpl automationScheduleService;

    @PostMapping("/task/automationschedules")
    @Operation(
            summary = "자동화 일정 추가",
            description = "카테고리ID, 자동화 내용, 종료 일시, 반복 주기, 반복 요일, 반복 일을 입력하면 자동화 일정이 추가된다."
    )
    @RequestBody(description = "자동화 일정 추가 요청 정보")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "자동화 일정이 성공적으로 추가된다.")
    public ResponseEntity<ApiResponse<Long>> createAutomationSchedule(
            @RequestBody @Valid AutomationScheduleCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        Long id = automationScheduleService.createAutomationSchedule(request, memberId);
        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @PutMapping("/task/automationschedules/{id}")
    @Operation(summary = "자동화 일정 수정", description = "자동화 일정 ID와 수정할 내용을 입력하면 수정일 이후 자동화 일정이 수정된다.")
    @Parameter(name = "id", description = "수정할 자동화 일정의 ID", example = "123")
    @RequestBody(description = "자동화 일정 수정 요청 정보")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "자동화 일정이 성공적으로 수정된다.")
    public ResponseEntity<ApiResponse<Void>> updateSchedule(
            @PathVariable("id") Long scheduleId,
            @RequestBody @Valid AutomationScheduleCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        automationScheduleService.updateAutomationSchedule(scheduleId, request, memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/task/automationschedules/{id}")
    @Operation(summary = "자동화 일정 삭제", description = "지정한 자동화 일정 ID 삭제일 이후의 모든 일정을 삭제한다.")
    @Parameter(name = "id", description = "삭제할 자동화 일정의 ID", example = "123")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "자동화 일정이 성공적으로 삭제된다.")
    public ResponseEntity<ApiResponse<Void>> deleteSchedulesAfter(
            @PathVariable("id") Long scheduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        automationScheduleService.deleteSchedulesAfter(scheduleId, memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}