package com.pigma.harusari.alarm.sse;

import com.pigma.harusari.alarm.exception.AlarmErrorCode;
import com.pigma.harusari.alarm.exception.AlarmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public SseEmitter subscribe(Long memberId) {
        // 기존 emitter가 있으면 종료
        if (emitters.containsKey(memberId)) {
            try {
                SseEmitter oldEmitter = emitters.get(memberId);
                oldEmitter.complete();
                log.warn("🔁 기존 SSE 연결 종료: memberId = {}", memberId);
            } catch (Exception e) {
                log.error("❌ 이전 SSE 종료 중 오류", e);
            }
            emitters.remove(memberId); // 혹시라도 complete가 호출되지 않을 때 확실히 제거
        }

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(memberId, emitter);
        log.info("📡 [Emitter 등록됨] memberId: {}, emitter: {}", memberId, emitter.hashCode());

        emitter.onCompletion(() -> {
            emitters.remove(memberId);
            log.info("✅ SSE 연결 완료 처리: memberId = {}", memberId);
        });

        emitter.onTimeout(() -> {
            emitters.remove(memberId);
            log.warn("⏰ SSE 연결 타임아웃: memberId = {}", memberId);
        });

        emitter.onError(e -> {
            emitters.remove(memberId);
            log.error("❗ SSE 연결 중 오류 발생: memberId = {}", memberId, e);
        });

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
            eventPublisher.publishEvent(new SseConnectedEvent(memberId));
        } catch (IOException e) {
            log.error("❌ SSE 초기 연결 실패", e);
            throw new AlarmException(AlarmErrorCode.SSE_CONNECTION_ERROR);
        }

        return emitter;
    }


    @Transactional(readOnly = true)
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
