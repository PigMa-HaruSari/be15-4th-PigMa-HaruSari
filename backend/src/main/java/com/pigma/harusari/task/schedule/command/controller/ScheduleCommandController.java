package com.pigma.harusari.task.schedule.command.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.exception.InvalidRepeatTypeException;
import com.pigma.harusari.task.exception.NeedLoginException;
import com.pigma.harusari.task.exception.TaskErrorCode;
import com.pigma.harusari.task.schedule.command.dto.request.CompletionStatusUpdateRequest;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleCreateRequest;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleUpdateRequest;
import com.pigma.harusari.task.schedule.command.dto.response.ScheduleCommandResponse;
import com.pigma.harusari.task.schedule.command.service.ScheduleCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "일정 API", description = "일정 추가, 수정, 삭제 API")
public class ScheduleCommandController {

    private final ScheduleCommandService scheduleCommandService;

    @PostMapping("/task/schedule")
    @Operation(summary = "일정 생성", description = "일정 생성 요청 정보를 입력받아 새로운 일정을 등록한다.")
    @RequestBody(description = "일정 생성 요청 정보")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "생성된 일정의 ID를 반환합니다.")
    public ResponseEntity<ApiResponse<Long>> createSchedule(
            @RequestBody @Valid ScheduleCreateRequest scheduleCreateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long memberId = userDetails.getMemberId();
        Long id = scheduleCommandService.createSchedule(scheduleCreateRequest, memberId);

        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @PutMapping("/task/schedule/{scheduleId}")
    @Operation(summary = "일정 수정", description = "일정 ID와 수정 요청 정보를 입력받아 일정을 수정한다.")
    @Parameter(name = "scheduleId", description = "수정할 일정의 ID", example = "123")
    @RequestBody(description = "일정 수정 요청 정보")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정이 성공적으로 수정된다.")
    public ResponseEntity<ApiResponse<Long>> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody @Valid ScheduleUpdateRequest scheduleUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new NeedLoginException(TaskErrorCode.NEED_LOGIN);
        }
        Long memberId = userDetails.getMemberId();
        if (memberId == null) {
            throw new InvalidRepeatTypeException(TaskErrorCode.INVALID_MEMBER_INFO);
        }

        Long updatedId = userDetails.getMemberId();
        scheduleCommandService.updateSchedule(scheduleId, scheduleUpdateRequest, memberId);
        return ResponseEntity.ok(ApiResponse.success(updatedId));
    }

    @DeleteMapping("/task/schedule/{scheduleId}")
    @Operation(summary = "일정 삭제", description = "일정 ID를 입력받아 해당 일정을 삭제한다.")
    @Parameter(name = "scheduleId", description = "삭제할 일정의 ID", example = "123")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정이 성공적으로 삭제된다.")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new NeedLoginException(TaskErrorCode.NEED_LOGIN);
        }
        Long memberId = userDetails.getMemberId();
        if (memberId == null) {
            throw new InvalidRepeatTypeException(TaskErrorCode.INVALID_MEMBER_INFO);
        }

        scheduleCommandService.deleteSchedule(scheduleId, memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/task/schedule/{scheduleId}/completionstatus")
    @Operation(summary = "일정 완료 상태 수정", description = "일정 ID와 완료 상태 값을 입력받아 해당 일정의 완료 상태를 수정한다.")
    @Parameter(name = "scheduleId", description = "완료 상태를 수정할 일정의 ID", example = "123")
    @RequestBody(description = "완료 상태 수정 요청 정보 (completionStatus: true/false)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정된 일정의 정보를 반환한다.")
    public ResponseEntity<ApiResponse<ScheduleCommandResponse>> updateCompletionStatus(
            @PathVariable Long scheduleId,
            @RequestBody @Valid CompletionStatusUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new NeedLoginException(TaskErrorCode.NEED_LOGIN);
        }
        Long memberId = userDetails.getMemberId();
        if (memberId == null) {
            throw new InvalidRepeatTypeException(TaskErrorCode.INVALID_MEMBER_INFO);
        }

        ScheduleCommandResponse response = scheduleCommandService.updateCompletionStatus(
                scheduleId, request.getCompletionStatus(), memberId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}


