package com.pigma.harusari.alarm.command.controller;

import com.pigma.harusari.alarm.command.service.AlarmCommandServiceImpl;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "알림 API", description = "알림 읽음 처리 관련 API")
public class AlarmCommandController {

    private final AlarmCommandServiceImpl alarmCommandService;

    @PutMapping("/alarms/read-all")
    @Operation(
            summary = "모든 알림 읽음 처리",
            description = "현재 로그인한 사용자의 모든 알림을 읽음 상태로 변경한다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 모든 알림이 읽음 처리되었음을 반환")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        alarmCommandService.markAllAsRead(memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
