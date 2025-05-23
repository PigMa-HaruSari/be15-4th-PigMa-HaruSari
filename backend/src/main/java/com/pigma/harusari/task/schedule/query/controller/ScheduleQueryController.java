package com.pigma.harusari.task.schedule.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.exception.InvalidRepeatTypeException;
import com.pigma.harusari.task.exception.NeedLoginException;
import com.pigma.harusari.task.exception.TaskErrorCode;
import com.pigma.harusari.task.schedule.query.dto.response.ScheduleListResponse;
import com.pigma.harusari.task.schedule.query.service.ScheduleQueryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ScheduleQueryController {

    private final ScheduleQueryServiceImpl scheduleQueryService;

    @GetMapping("/task/schedule")
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
