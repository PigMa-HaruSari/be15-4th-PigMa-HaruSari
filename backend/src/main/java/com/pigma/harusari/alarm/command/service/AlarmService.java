package com.pigma.harusari.alarm.command.service;

import com.pigma.harusari.alarm.command.dto.AlarmCreateDto;
import com.pigma.harusari.alarm.command.entity.Alarm;

public interface AlarmService {
    Alarm createAlarm(AlarmCreateDto dto);
}
