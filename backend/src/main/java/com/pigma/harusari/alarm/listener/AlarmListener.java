package com.pigma.harusari.alarm.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.alarm.command.entity.Alarm;
import com.pigma.harusari.alarm.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmListener {

    private final SseService sseService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "alarm.queue")
    public void receive(Alarm alarm) {
        log.info("📨 Received alarm: {}", alarm.getAlarmMessage());

        // SSE 전송
        sseService.send(alarm.getMemberId(), alarm.getAlarmMessage());
    }

}
