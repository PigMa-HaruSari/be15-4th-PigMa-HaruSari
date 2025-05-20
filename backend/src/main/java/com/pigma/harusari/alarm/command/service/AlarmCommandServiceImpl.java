package com.pigma.harusari.alarm.command.service;

import com.pigma.harusari.alarm.command.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmCommandServiceImpl implements AlarmCommandService {

    private final AlarmRepository alarmRepository;

    @Override
    @Transactional
    public void markAllAsRead(Long memberId) {
        alarmRepository.markAllByMemberIdAsRead(memberId);
    }
}
