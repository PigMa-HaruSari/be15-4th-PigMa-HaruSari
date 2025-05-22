package com.pigma.harusari.task.schedule.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.schedule.query.dto.request.ScheduleSearchRequest;
import com.pigma.harusari.task.schedule.query.dto.response.ScheduleListResponse;
import com.pigma.harusari.task.schedule.query.service.ScheduleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ScheduleQueryController {

    private final ScheduleQueryService scheduleQueryService;

//    @GetMapping("/schedule")
//    public ResponseEntity<ApiResponse<ScheduleListResponse>> getScheduleList(
//            @ModelAttribute ScheduleSearchRequest scheduleSearchRequest
//    ) {
//            ScheduleListResponse response = scheduleQueryService.getScheduleList(scheduleSearchRequest);
//            return ResponseEntity.ok(ApiResponse.success(response));
//    }

    // 사용자 권한 추가

    @GetMapping("/schedule")
    public ResponseEntity<ApiResponse<ScheduleListResponse>> getScheduleList(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Long memberId = userDetails.getMemberId();
        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 정보가 올바르지 않습니다.");
        }

        // memberId를 파라미터로 전달
        ScheduleListResponse response = scheduleQueryService.getScheduleList(categoryId, memberId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
