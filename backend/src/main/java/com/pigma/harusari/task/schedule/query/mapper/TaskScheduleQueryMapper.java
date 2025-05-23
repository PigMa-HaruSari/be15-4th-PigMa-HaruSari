package com.pigma.harusari.task.schedule.query.mapper;

import com.pigma.harusari.task.schedule.command.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TaskScheduleQueryMapper {

    List<Schedule> getLastMonthSchedules(@Param("memberId") Long memberId,
                                         @Param("start") LocalDate start,
                                         @Param("end") LocalDate end);

    int countCompletedSchedules(@Param("memberId") Long memberId,
                                @Param("start") LocalDate start,
                                @Param("end") LocalDate end);
}
