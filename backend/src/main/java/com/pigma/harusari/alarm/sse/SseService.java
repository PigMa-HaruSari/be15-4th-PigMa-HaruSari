package com.pigma.harusari.alarm.sse;

import com.pigma.harusari.alarm.exception.AlarmErrorCode;
import com.pigma.harusari.alarm.exception.AlarmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(memberId, emitter);

        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));
        emitter.onError(e -> {
            log.error("❗ SSE 연결 중 오류 발생", e);
            emitters.remove(memberId);
        });

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
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
