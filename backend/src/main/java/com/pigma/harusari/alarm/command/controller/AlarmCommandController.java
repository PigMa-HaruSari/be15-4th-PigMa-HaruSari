package com.pigma.harusari.alarm.command.controller;

import com.pigma.harusari.alarm.command.service.AlarmCommandServiceImpl;
import com.pigma.harusari.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AlarmCommandController {

    private final AlarmCommandServiceImpl alarmCommandService;

    @PutMapping("/alarms/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@AuthenticationPrincipal User userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        alarmCommandService.markAllAsRead(memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
