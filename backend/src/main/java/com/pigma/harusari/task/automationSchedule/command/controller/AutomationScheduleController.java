package com.pigma.harusari.task.automationSchedule.command.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.automationSchedule.command.dto.request.AutomationScheduleCreateRequest;
import com.pigma.harusari.task.automationSchedule.command.service.AutomationScheduleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AutomationScheduleController {

    private final AutomationScheduleServiceImpl automationScheduleService;

    @PostMapping("/task/automationschedules")
    public ResponseEntity<ApiResponse<Long>> createAutomationSchedule(
            @RequestBody @Valid AutomationScheduleCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        Long id = automationScheduleService.createAutomationSchedule(request, memberId);
        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @PutMapping("/task/automationschedules/{id}")
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
    public ResponseEntity<ApiResponse<Void>> deleteSchedulesAfter(
            @PathVariable("id") Long scheduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        automationScheduleService.deleteSchedulesAfter(scheduleId, memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}