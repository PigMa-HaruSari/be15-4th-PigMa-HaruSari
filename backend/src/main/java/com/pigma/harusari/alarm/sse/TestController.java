package com.pigma.harusari.alarm.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigma.harusari.alarm.command.dto.AlarmCreateDto;
import com.pigma.harusari.alarm.command.entity.Alarm;
import com.pigma.harusari.alarm.command.scheduler.AlarmScheduler;
import com.pigma.harusari.alarm.command.service.AlarmService;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class    TestController {

    private final AlarmService alarmService;
    private final RabbitTemplate rabbitTemplate;
    private final AlarmScheduler alarmScheduler;

    @GetMapping("/send")
    public String sendTestMessage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId(); // 또는 getUserId() 메서드 사용
        AlarmCreateDto dto = AlarmCreateDto.builder()
                .memberId(memberId)
                .alarmMessage("🔥 실 사용자에게 알림")
                .type("TEST")
                .build();

        Alarm alarm = alarmService.createAlarm(dto);
        rabbitTemplate.convertAndSend("alarm.exchange", "alarm.key", alarm);

        return "로그인 사용자에게 알림 전송 완료";
    }

    @PostMapping("/daily")
    public String triggerDailyAlarm(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId(); // 또는 getUserId() 메서드 사용
        alarmScheduler.sendDailyUncompletedTaskAlarm(memberId);
        return "✅ Daily uncompleted task alarm triggered for userId: " + memberId;
    }

    @PostMapping("/weekly")
    public String triggerWeeklyAlarm(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        alarmScheduler.sendWeeklyAchievementAlarm(memberId);
        return "✅ Weekly achievement alarm triggered for userId: " + memberId;
    }

    @PostMapping("/monthly")
    public String triggerMonthlyAlarm(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        alarmScheduler.sendMonthlyAchievementAlarm(memberId);
        return "✅ Monthly achievement alarm triggered for userId: " + memberId;
    }


    @PostMapping("/test/daily")
    public String triggerDailyAlarm() {
        alarmScheduler.sendDailyUncompletedTaskAlarm();
        return "✅ Daily uncompleted task alarm triggered.";
    }

    @PostMapping("/test/weekly")
    public String triggerWeeklyAlarm() {
        alarmScheduler.sendWeeklyAchievementAlarm();
        return "✅ Weekly achievement alarm triggered.";
    }
}