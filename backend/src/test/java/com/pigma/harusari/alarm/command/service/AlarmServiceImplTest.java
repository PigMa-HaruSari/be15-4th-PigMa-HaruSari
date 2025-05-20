package com.pigma.harusari.alarm.command.service;

import com.pigma.harusari.alarm.command.dto.AlarmCreateDto;
import com.pigma.harusari.alarm.command.entity.Alarm;
import com.pigma.harusari.alarm.command.repository.AlarmRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[알림 - service] AlarmServiceImplTest 테스트")
class AlarmServiceImplTest {

    @Mock
    private AlarmRepository alarmRepository;

    @InjectMocks
    private AlarmServiceImpl alarmService;

    @Test
    @DisplayName("[알림] 알림 생성 성공 테스트")
    void testCreateAlarm() {
        // given
        AlarmCreateDto dto = AlarmCreateDto.builder()
                .memberId(1L)
                .alarmMessage("테스트 알림입니다.")
                .type("DAILY")
                .build();

        Alarm fakeAlarm = Alarm.builder()
                .memberId(dto.getMemberId())
                .alarmMessage(dto.getAlarmMessage())
                .type(dto.getType())
                .isRead(false)
                .createdAt(new Date())
                .build();

        when(alarmRepository.save(any(Alarm.class))).thenReturn(fakeAlarm);

        // when
        Alarm savedAlarm = alarmService.createAlarm(dto);

        // then
        ArgumentCaptor<Alarm> alarmCaptor = ArgumentCaptor.forClass(Alarm.class);
        verify(alarmRepository, times(1)).save(alarmCaptor.capture());

        Alarm capturedAlarm = alarmCaptor.getValue();

        assertThat(savedAlarm).isNotNull();
        assertThat(savedAlarm.getMemberId()).isEqualTo(dto.getMemberId());
        assertThat(savedAlarm.getAlarmMessage()).isEqualTo(dto.getAlarmMessage());
        assertThat(savedAlarm.getType()).isEqualTo(dto.getType());
        assertThat(savedAlarm.getIsRead()).isFalse();

        assertThat(capturedAlarm.getMemberId()).isEqualTo(dto.getMemberId());
        assertThat(capturedAlarm.getAlarmMessage()).isEqualTo(dto.getAlarmMessage());
        assertThat(capturedAlarm.getType()).isEqualTo(dto.getType());
        assertThat(capturedAlarm.getIsRead()).isFalse();
        assertThat(capturedAlarm.getCreatedAt()).isNotNull();
    }
}
