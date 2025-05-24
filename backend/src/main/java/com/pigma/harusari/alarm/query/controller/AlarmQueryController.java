package com.pigma.harusari.alarm.query.controller;

import com.pigma.harusari.alarm.query.dto.AlarmResponseDto;
import com.pigma.harusari.alarm.query.service.AlarmQueryService;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
@Tag(name = "알림 조회 API", description = "알림 조회 관련 API")
public class AlarmQueryController {

    private final AlarmQueryService alarmQueryService;

    @GetMapping("/unread")
    @Operation(
            summary = "읽지 않은 알림 조회",
            description = "현재 로그인한 사용자의 읽지 않은 모든 알림 목록을 반환한다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "읽지 않은 알림 목록 반환 성공")
    public ResponseEntity<ApiResponse<List<AlarmResponseDto>>> getUnreadAlarms(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();

        List<AlarmResponseDto> alarms = alarmQueryService.getUnreadAlarms(memberId);
        return ResponseEntity.ok(ApiResponse.success(alarms));
    }

}
