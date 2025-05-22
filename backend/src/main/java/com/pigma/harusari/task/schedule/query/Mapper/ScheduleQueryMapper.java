package com.pigma.harusari.task.schedule.query.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleQueryMapper {

    List<Schedule> getLastMonthSchedules(@Param("userId") Long userId,
                                         @Param("start") LocalDate start,
                                         @Param("end") LocalDate end);

    int countCompletedSchedules(@Param("schedules") List<Schedule> schedules);
}
