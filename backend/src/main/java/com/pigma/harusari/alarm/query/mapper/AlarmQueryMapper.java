package com.pigma.harusari.alarm.query.mapper;

import com.pigma.harusari.alarm.query.dto.AlarmResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmQueryMapper {
    List<AlarmResponseDto> findUnreadAlarmsByMemberId(Long memberId);
}
