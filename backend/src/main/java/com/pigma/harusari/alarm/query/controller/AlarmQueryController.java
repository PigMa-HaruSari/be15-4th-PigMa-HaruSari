package com.pigma.harusari.alarm.query.controller;

import com.pigma.harusari.alarm.query.dto.AlarmResponseDto;
import com.pigma.harusari.alarm.query.service.AlarmQueryService;
import com.pigma.harusari.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmQueryController {

    private final AlarmQueryService alarmQueryService;

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<AlarmResponseDto>>> getUnreadAlarms(@AuthenticationPrincipal User userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        List<AlarmResponseDto> alarms = alarmQueryService.getUnreadAlarms(memberId);
        return ResponseEntity.ok(ApiResponse.success(alarms));
    }

}
