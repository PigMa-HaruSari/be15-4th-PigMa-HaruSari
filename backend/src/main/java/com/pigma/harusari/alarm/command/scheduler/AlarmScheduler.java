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

    @Scheduled(cron = "0 0 8 * * MON") // 매주 월요일 오전 8시
    public void sendWeeklyAchievementAlarm() {
        List<Map<String, Object>> stats = scheduleQueryMapper.findWeeklyAchievementRate();

        for (Map<String, Object> stat : stats) {
            Long memberId = ((Number) stat.get("member_id")).longValue();
            int total = ((Number) stat.get("total")).intValue();
            int completed = ((Number) stat.get("completed")).intValue();

            int percentage = (total == 0) ? 0 : (completed * 100 / total);

            AlarmCreateDto dto = AlarmCreateDto.builder()
                    .memberId(memberId)
                    .alarmMessage("지난 주의 일정 달성률은 " + percentage + "% 입니다! 💪")
                    .type("WEEKLY")
                    .build();

            var alarm = alarmService.createAlarm(dto);
            rabbitTemplate.convertAndSend("alarm.exchange", "alarm.key", alarm);
        }

        log.info("📅 Weekly achievement alarms sent to {} users", stats.size());
    }
}
