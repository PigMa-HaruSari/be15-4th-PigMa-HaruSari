package com.pigma.harusari.alarm.command.service;

import com.pigma.harusari.alarm.command.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmTransactionalService {

    private final AlarmRepository alarmRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAlarmAsSent(Long alarmId) {
        alarmRepository.markAsSent(alarmId);
    }
}