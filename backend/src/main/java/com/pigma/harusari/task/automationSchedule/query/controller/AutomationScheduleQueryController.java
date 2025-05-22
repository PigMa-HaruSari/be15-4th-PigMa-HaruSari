package com.pigma.harusari.task.automationSchedule.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.task.automationSchedule.command.dto.response.AutomationScheduleResponse;
import com.pigma.harusari.task.automationSchedule.command.service.AutomationScheduleServiceImpl;
import com.pigma.harusari.task.automationSchedule.query.dto.request.AutomationScheduleRequest;
import com.pigma.harusari.task.automationSchedule.query.dto.response.AutomationScheduleDto;
import com.pigma.harusari.task.automationSchedule.query.service.AutomationScheduleQueryService;
import com.pigma.harusari.task.schedule.command.entity.Schedule;
import com.pigma.harusari.task.schedule.command.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AutomationScheduleQueryController {

    private final AutomationScheduleQueryService automationScheduleQueryService;
    private final AutomationScheduleServiceImpl automationScheduleService;
    private final ScheduleRepository scheduleRepository;


    @GetMapping("/automationSchedules")
    public ResponseEntity<List<AutomationScheduleDto>> getAutomationSchedules(
            AutomationScheduleRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        List<AutomationScheduleDto> schedules = automationScheduleQueryService.getAutomationScheduleList(request, memberId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/automationSchedules/nearest")
    public ResponseEntity<Map<String, Object>> getNearestSchedule(
            @RequestParam("automationScheduleId") Long automationScheduleId,
            @RequestParam("baseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long memberId = userDetails.getMemberId();

        // 1. 기존 자동화 일정 조회 (memberId로 소유자 검증 등)
        AutomationScheduleResponse scheduleDto = automationScheduleService.getAutomationSchedule(automationScheduleId, memberId);

        // 2. 가장 가까운 일정 조회
        Schedule nearest = scheduleRepository
                .findFirstByAutomationSchedule_AutomationScheduleIdAndScheduleDateGreaterThanEqualOrderByScheduleDateAsc(automationScheduleId, baseDate)
                .orElse(null);

        // 3. Map에 담아 반환
        Map<String, Object> result = new HashMap<>();
        result.put("automationSchedule", scheduleDto);
        result.put("nearestSchedule", nearest);

        return ResponseEntity.ok(result);
    }

}
