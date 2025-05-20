package com.pigma.harusari.alarm.sse;

import com.pigma.harusari.alarm.exception.AlarmErrorCode;
import com.pigma.harusari.alarm.exception.AlarmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[SSE - service] SseService 테스트")
class SseServiceTest {

    private SseService sseService;

    @BeforeEach
    void setUp() {
        sseService = new SseService();
    }

    @Test
    @DisplayName("[구독] - 정상 구독 시 SseEmitter 반환 및 관리")
    void subscribe_success() throws IOException {
        Long memberId = 1L;
        SseEmitter emitter = sseService.subscribe(memberId);

        assertNotNull(emitter);
        // 내부 emitters 맵에 저장됐는지 확인 (리플렉션 대신 getter가 없으면 간접 검증)
        // 여기서는 내부 구조에 접근하지 않고, send 테스트에서 검증 가능

        // connect 이벤트 전송 테스트 (SseEmitter.send가 IOException 발생하지 않으면 성공)
        // 직접 확인은 어렵지만 예외 안 나면 성공으로 본다
    }

    @Test
    @DisplayName("[전송] - 정상 전송 시 예외 없음")
    void send_success() throws IOException {
        Long memberId = 1L;
        SseEmitter emitter = sseService.subscribe(memberId);

        assertDoesNotThrow(() -> sseService.send(memberId, "테스트 메시지"));
    }

    @Test
    @DisplayName("[예외처리] - 존재하지 않는 emitter 전송 시 AlarmException SSE_NOT_FOUND 발생")
    void send_emitterNotFound_throwsException() {
        Long memberId = 999L; // 구독하지 않은 ID

        AlarmException exception = assertThrows(AlarmException.class,
                () -> sseService.send(memberId, "메시지"));

        assertEquals(AlarmErrorCode.SSE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("[예외처리] - 전송 중 IOException 발생 시 AlarmException SSE_SEND_ERROR 발생")
    void send_ioException_throwsException() throws IOException {
        Long memberId = 1L;

        // 구독해서 emitter 추가
        SseEmitter emitter = spy(sseService.subscribe(memberId));

        // spy emitter의 send 호출 시 IOException 던지도록 설정
        doThrow(new IOException("Send error")).when(emitter).send(any(SseEmitter.event().getClass()));

        // 실제 emitters 맵에 스파이 emitter를 덮어쓰기
        Map<Long, SseEmitter> emitters = getEmittersMap(sseService);
        emitters.put(memberId, emitter);

        AlarmException exception = assertThrows(AlarmException.class,
                () -> sseService.send(memberId, "메시지"));

        assertEquals(AlarmErrorCode.SSE_SEND_ERROR, exception.getErrorCode());

        // emitter가 삭제됐는지 확인
        assertFalse(emitters.containsKey(memberId));
    }

    // 리플렉션으로 private emitters 필드 접근 (테스트용)
    @SuppressWarnings("unchecked")
    private Map<Long, SseEmitter> getEmittersMap(SseService sseService) {
        try {
            var field = SseService.class.getDeclaredField("emitters");
            field.setAccessible(true);
            return (Map<Long, SseEmitter>) field.get(sseService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
