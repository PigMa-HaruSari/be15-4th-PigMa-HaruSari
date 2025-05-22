package com.pigma.harusari.task.schedule.command.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.schedule.command.dto.request.CompletionStatusUpdateRequest;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleCreateRequest;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleUpdateRequest;
import com.pigma.harusari.task.schedule.command.dto.response.ScheduleCommandResponse;
import com.pigma.harusari.task.schedule.command.service.ScheduleCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ScheduleCommandController {

    private final ScheduleCommandService scheduleCommandService;


    @PostMapping("/schedule")
    public ResponseEntity<ApiResponse<Long>> createSchedule(
            @RequestBody @Valid ScheduleCreateRequest scheduleCreateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long memberId = userDetails.getMemberId();
        Long id = scheduleCommandService.createSchedule(scheduleCreateRequest, memberId);

        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @PutMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResponse<Long>> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody @Valid ScheduleUpdateRequest scheduleUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Long memberId = userDetails.getMemberId();
        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 정보가 올바르지 않습니다.");
        }

        Long updatedId = userDetails.getMemberId();
        scheduleCommandService.updateSchedule(scheduleId, scheduleUpdateRequest, memberId);
        return ResponseEntity.ok(ApiResponse.success(updatedId));
    }

    @DeleteMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Long memberId = userDetails.getMemberId();
        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 정보가 올바르지 않습니다.");
        }

        scheduleCommandService.deleteSchedule(scheduleId, memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PutMapping("/schedule/{scheduleId}/completionStatus")
    public ResponseEntity<ApiResponse<ScheduleCommandResponse>> updateCompletionStatus(
            @PathVariable Long scheduleId,
            @RequestBody @Valid CompletionStatusUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Long memberId = userDetails.getMemberId();
        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 정보가 올바르지 않습니다.");
        }

        ScheduleCommandResponse response = scheduleCommandService.updateCompletionStatus(
                scheduleId, request.getCompletionStatus(), memberId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}


