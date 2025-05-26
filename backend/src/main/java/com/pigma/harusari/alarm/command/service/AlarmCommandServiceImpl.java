package com.pigma.harusari.alarm.command.service;

import com.pigma.harusari.alarm.command.entity.Alarm;
import com.pigma.harusari.alarm.command.repository.AlarmRepository;
import com.pigma.harusari.alarm.exception.AlarmException;
import com.pigma.harusari.alarm.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmCommandServiceImpl implements AlarmCommandService {

    private final AlarmRepository alarmRepository;
    private final SseService sseService;
    private final AlarmTransactionalService alarmTransactionalService;

    @Override
    @Transactional(readOnly = true)
    public void markAllAsRead(Long memberId) {
        alarmRepository.markAllByMemberIdAsRead(memberId);
    }

    @Async
    @Override
    @Transactional(readOnly = true)
    public void sendUnsentAlarms(Long memberId) {
        List<Alarm> unsentAlarms = alarmRepository.findUnsentAlarmsByMemberId(memberId);

        for (Alarm alarm : unsentAlarms) {
            try {
                // 전송되었으므로 상태 변경 & 외부 빈을 통해 REQUIRES_NEW 트랜잭션 적용
                sseService.send(memberId, alarm.getAlarmMessage());
                alarmTransactionalService.markAlarmAsSent(alarm.getAlarmId());
            } catch (AlarmException e) {
                // SSE 연결이 없는 경우 무시하고 다음 알림으로 진행
                // 이 사용자는 아직 SSE 연결을 하지 않았을 수 있으므로 다음 로그인이나 페이지 전환에 다시 시도됨
                log.warn("SSE 전송 실패, 다음 기회에 다시 전송 예정: {}", alarm.getAlarmId());
            }
        }
    }
}
