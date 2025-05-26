package com.pigma.harusari.alarm.sse;

import com.pigma.harusari.alarm.command.service.AlarmCommandService;
import com.pigma.harusari.alarm.exception.AlarmErrorCode;
import com.pigma.harusari.alarm.exception.AlarmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(memberId, emitter);
        System.out.println("📡 [Emitter 등록됨] memberId: " + memberId + ", emitter: " + emitter.hashCode());

        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));
        emitter.onError(e -> {
            log.error("❗ SSE 연결 중 오류 발생", e);
            emitters.remove(memberId);
        });

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));

            // 🔁 이 부분을 직접 호출 대신 이벤트 발행으로
            eventPublisher.publishEvent(new SseConnectedEvent(memberId));

        } catch (IOException e) {
            log.error("❌ Connection error", e);
            throw new AlarmException(AlarmErrorCode.SSE_CONNECTION_ERROR);
        }

        return emitter;
    }

    public void send(Long memberId, String message) {
        SseEmitter emitter = emitters.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("alarm").data(message));
            } catch (IOException e) {
                log.error("❌ Failed to send SSE", e);
                emitters.remove(memberId);
                throw new AlarmException(AlarmErrorCode.SSE_SEND_ERROR);
            }
        } else {
            throw new AlarmException(AlarmErrorCode.SSE_NOT_FOUND);
        }
    }
}
