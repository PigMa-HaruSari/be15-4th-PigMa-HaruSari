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
        List<Map<String, Object>> counts = scheduleQueryMapper.findIncompleteScheduleCount();

        for (int i = 0; i < counts.size(); i++) {
            Map<String, Object> result = counts.get(i);
            Long memberId = ((Number) result.get("member_id")).longValue();
            int count = ((Number) result.get("count")).intValue();

            AlarmCreateDto dto = AlarmCreateDto.builder()
                    .memberId(memberId)
                    .alarmMessage("아직 완료하지 않은 오늘 일정이 " + count + "개 있어요! 💡")
                    .type("DAILY")
                    .build();

            var alarm = alarmService.createAlarm(dto);
            rabbitTemplate.convertAndSend("alarm.exchange", "alarm.key", alarm);
        }

        log.info("⏰ Daily grouped alarm sent to {} users", counts.size());
    }
}
