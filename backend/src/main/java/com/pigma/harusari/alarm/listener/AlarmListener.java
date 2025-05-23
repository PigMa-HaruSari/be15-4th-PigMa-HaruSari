package com.pigma.harusari.alarm.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.alarm.command.entity.Alarm;
import com.pigma.harusari.alarm.command.repository.AlarmRepository;
import com.pigma.harusari.alarm.exception.AlarmException;
import com.pigma.harusari.alarm.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmListener {

    private final SseService sseService;
    private final ObjectMapper objectMapper;
    private final AlarmRepository alarmRepository;

    @Transactional
    @RabbitListener(queues = "alarm.queue")
    public void receive(Alarm alarm) {
        log.info("📨 Received alarm: {}", alarm.getAlarmMessage());

        try {
            // SSE 전송 시도
            sseService.send(alarm.getMemberId(), alarm.getAlarmMessage());

            // ✅ 전송 성공 시 isSent 업데이트
            alarmRepository.markAsSent(alarm.getAlarmId());
        } catch (AlarmException e) {
            // SSE 연결이 없는 경우 → 경고만 남기고 메시지 정상 소비 처리
            log.warn("❗ 알림 전송 실패 (SSE 연결 없음): memberId={}, message={}", alarm.getMemberId(), alarm.getAlarmMessage());
        } catch (Exception e) {
            // 예기치 못한 에러는 재시도 되도록 전파
            log.error("❌ 알림 처리 중 오류 발생", e);
            throw e;
        }
    }
}
