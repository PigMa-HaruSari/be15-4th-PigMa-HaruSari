package com.pigma.harusari.alarm.command.service;

import com.pigma.harusari.alarm.command.dto.AlarmCreateDto;
import com.pigma.harusari.alarm.command.entity.Alarm;
import com.pigma.harusari.alarm.command.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Alarm createAlarm(AlarmCreateDto dto) {
        Alarm alarm = Alarm.builder()
                .memberId(dto.getMemberId())
                .alarmMessage(dto.getAlarmMessage())
                .type(dto.getType())
                .isRead(false)
                .createdAt(new Date())
                .build();
        return alarmRepository.save(alarm);
    }
}
