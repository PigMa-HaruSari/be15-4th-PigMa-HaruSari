package com.pigma.harusari.task.schedule.query.mapper;

import com.pigma.harusari.task.schedule.query.dto.response.ScheduleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    List<ScheduleDto> selectScheduleByMemberId(
            @Param("categoryId") Long categoryId,
            @Param("scheduleDate") LocalDate scheduleDate,
            @Param("memberId") Long memberId
    );

}