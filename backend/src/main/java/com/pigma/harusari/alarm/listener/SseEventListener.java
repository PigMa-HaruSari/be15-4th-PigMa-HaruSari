package com.pigma.harusari.alarm.listener;

import com.pigma.harusari.alarm.command.service.AlarmCommandService;
import com.pigma.harusari.alarm.sse.SseConnectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseEventListener {

    private final AlarmCommandService alarmCommandService;

    @Async
    @EventListener
    public void handleSseConnected(SseConnectedEvent event) {
        Long memberId = event.memberId();
        alarmCommandService.sendUnsentAlarms(memberId);
    }
}
