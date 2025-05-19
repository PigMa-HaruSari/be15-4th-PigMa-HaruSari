package com.pigma.harusari.alarm.query.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleQueryMapper {
    List<Map<String, Object>> findIncompleteSchedules();
}
