package com.pigma.harusari.task.automationSchedule.query.mapper;

import com.pigma.harusari.task.automationSchedule.query.dto.request.AutomationScheduleRequest;
import com.pigma.harusari.task.automationSchedule.query.dto.response.AutomationScheduleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AutomationScheduleMapper {
    List<AutomationScheduleDto> selectAutomationScheduleList(
            @Param("request") AutomationScheduleRequest request,
            @Param("memberId") Long memberId);

    List<AutomationScheduleDto> findAll(@Param("memberId") Long memberId, @Param("categoryId") Long categoryId);

    AutomationScheduleDto findById(@Param("scheduleId") Long scheduleId);
}
