package com.pigma.harusari.statistics.query.dto.response;

import lombok.Builder;

@Builder
public record StatisticsDailyRateResponse(
    double achievementRate
) {
}