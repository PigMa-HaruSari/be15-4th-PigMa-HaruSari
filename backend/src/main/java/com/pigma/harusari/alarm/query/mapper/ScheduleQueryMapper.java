package com.pigma.harusari.alarm.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleQueryMapper {
    List<Map<String, Object>> findIncompleteScheduleCount();

    List<Map<String, Object>> findWeeklyAchievementRate();

    List<Map<String, Object>> findMonthlyAchievementRate();


    // 테스트용 단일 사용자 쿼리
    Map<String, Object> findIncompleteScheduleCountByMemberId(@Param("memberId") Long memberId);
    Map<String, Object> findWeeklyAchievementRateByMemberId(@Param("memberId") Long memberId);
    Map<String, Object> findMonthlyAchievementRateByMemberId(@Param("memberId") Long memberId);
}
