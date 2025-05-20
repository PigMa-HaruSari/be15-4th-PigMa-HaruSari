package com.pigma.harusari.alarm.query.service;

import com.pigma.harusari.alarm.query.dto.AlarmResponseDto;
import com.pigma.harusari.alarm.query.mapper.AlarmQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmQueryService {

    private final AlarmQueryMapper alarmQueryMapper;

    public List<AlarmResponseDto> getUnreadAlarms(Long memberId) {
        return alarmQueryMapper.findUnreadAlarmsByMemberId(memberId);
    }
}
