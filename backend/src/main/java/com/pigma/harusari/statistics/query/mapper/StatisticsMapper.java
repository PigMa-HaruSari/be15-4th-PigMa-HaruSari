package com.pigma.harusari.statistics.query.mapper;

import com.pigma.harusari.statistics.query.dto.response.StatisticsDayResponse;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface StatisticsMapper {

    StatisticsDayResponse findStatisticsDaily(Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}