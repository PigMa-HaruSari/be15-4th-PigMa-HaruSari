package com.pigma.harusari.alarm.command.service;

import static org.mockito.Mockito.verify;

import com.pigma.harusari.alarm.command.repository.AlarmRepository;
import com.pigma.harusari.alarm.command.service.AlarmCommandServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("[알림 - service] AlarmCommandServiceImplTest 테스트")
class AlarmCommandServiceImplTest {

    @Mock
    private AlarmRepository alarmRepository;

    @InjectMocks
    private AlarmCommandServiceImpl alarmCommandService;

    @Test
    void testMarkAllAsRead() {
        Long memberId = 1L;

        alarmCommandService.markAllAsRead(memberId);

        verify(alarmRepository).markAllByMemberIdAsRead(memberId);
    }
}
