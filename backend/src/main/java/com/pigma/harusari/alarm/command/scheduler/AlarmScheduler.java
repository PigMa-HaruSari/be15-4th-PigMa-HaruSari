package com.pigma.harusari.alarm.command.scheduler;

import com.pigma.harusari.alarm.command.dto.AlarmCreateDto;
import com.pigma.harusari.alarm.command.service.AlarmService;
import com.pigma.harusari.alarm.query.mapper.ScheduleQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmScheduler {

    private final ScheduleQueryMapper scheduleQueryMapper;
    private final AlarmService alarmService;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0 22 * * *") // 매일 오후 10시
    public void sendDailyUncompletedTaskAlarm() {
        List<Map<String, Object>> schedules = scheduleQueryMapper.findIncompleteSchedules();

        for (Map<String, Object> schedule : schedules) {
            Long memberId = ((Number) schedule.get("member_uid")).longValue();

            AlarmCreateDto dto = AlarmCreateDto.builder()
                    .memberId(memberId)
                    .alarmMessage("미완료된 일정이 있습니다. 확인해 주세요!")
                    .type("DAILY")
                    .build();

            // DB 저장
            var alarm = alarmService.createAlarm(dto);

            // 큐 전송
            rabbitTemplate.convertAndSend("alarm.exchange", "alarm.key", alarm);
        }

        log.info("⏰ Daily alarm sent to {} users", schedules.size());
    }
}
