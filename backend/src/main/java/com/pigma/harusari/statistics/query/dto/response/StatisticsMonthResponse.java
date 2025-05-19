package com.pigma.harusari.statistics.query.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record StatisticsMonthResponse(
        String type,
        LocalDate date,
        double achievementRate
) {
}