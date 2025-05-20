package com.pigma.harusari.alarm.query.service;

import com.pigma.harusari.alarm.query.dto.AlarmResponseDto;
import com.pigma.harusari.alarm.query.mapper.AlarmQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("[알림 - service] AlarmQueryService 테스트")
class AlarmQueryServiceTest {

    @Mock
    private AlarmQueryMapper alarmQueryMapper;

    @InjectMocks
    private AlarmQueryService alarmQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[알림] 알림 전송 성공 테스트")
    void testGetUnreadAlarms() {
        // given
        Long memberId = 1L;
        AlarmResponseDto dto = AlarmResponseDto.builder()
                .alarmId(100L)
                .alarmMessage("테스트 알림 메시지")
                .type("DAILY")
                .isRead(false)
                .createdAt(new Date())
                .build();

        given(alarmQueryMapper.findUnreadAlarmsByMemberId(memberId)).willReturn(List.of(dto));

        // when
        List<AlarmResponseDto> result = alarmQueryService.getUnreadAlarms(memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getAlarmId()).isEqualTo(100L);
        assertThat(result.get(0).getAlarmMessage()).isEqualTo("테스트 알림 메시지");
        assertThat(result.get(0).getType()).isEqualTo("DAILY");
        assertThat(result.get(0).getIsRead()).isFalse();
    }
}
