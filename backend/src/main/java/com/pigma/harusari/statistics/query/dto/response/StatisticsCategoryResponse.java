package com.pigma.harusari.statistics.query.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record StatisticsCategoryResponse(
        String type,
        List<StatisticsCategoryRateResponse> categoryRates
) {
}