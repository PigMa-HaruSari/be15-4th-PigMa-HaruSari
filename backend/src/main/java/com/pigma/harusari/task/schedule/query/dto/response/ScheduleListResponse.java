package com.pigma.harusari.task.schedule.query.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScheduleListResponse {
    private final List<ScheduleDto> schedule;

}
