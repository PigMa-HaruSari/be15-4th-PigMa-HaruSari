package com.pigma.harusari.alarm.sse;

import com.pigma.harusari.alarm.exception.AlarmErrorCode;
import com.pigma.harusari.alarm.exception.AlarmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[SSE - service] SseService 테스트")
class SseServiceTest {

    private SseService sseService;
    private ApplicationEventPublisher mockPublisher;

    @BeforeEach
    void setUp() {
        mockPublisher = mock(ApplicationEventPublisher.class);
        sseService = new SseService(mockPublisher);
    }

    @Test
    @DisplayName("[구독] - 정상 구독 시 SseEmitter 반환 및 관리")
    void subscribe_success() {
        Long memberId = 1L;
        SseEmitter emitter = sseService.subscribe(memberId);

        assertNotNull(emitter);

        // ApplicationEventPublisher가 이벤트를 발행했는지 확인
        verify(mockPublisher, times(1)).publishEvent(new SseConnectedEvent(memberId));
    }

    @Test
    @DisplayName("[전송] - 정상 전송 시 예외 없음")
    void send_success() {
        Long memberId = 1L;
        sseService.subscribe(memberId);

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

        // 먼저 구독해서 emitter 저장
        SseEmitter realEmitter = sseService.subscribe(memberId);

        // spy emitter로 감싸기
        SseEmitter spyEmitter = spy(realEmitter);
        doThrow(new IOException("Send error")).when(spyEmitter).send(any(SseEmitter.SseEventBuilder.class));

        // emitters 맵에 스파이 emitter를 덮어쓰기
        Map<Long, SseEmitter> emitters = getEmittersMap(sseService);
        emitters.put(memberId, spyEmitter);

        AlarmException exception = assertThrows(AlarmException.class,
                () -> sseService.send(memberId, "메시지"));

        assertEquals(AlarmErrorCode.SSE_SEND_ERROR, exception.getErrorCode());

        // 예외 후 emitter가 삭제됐는지 확인
        assertFalse(emitters.containsKey(memberId));
    }

    // 리플렉션으로 private emitters 맵 접근
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
