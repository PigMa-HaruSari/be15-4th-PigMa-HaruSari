package com.pigma.harusari.alarm.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class AlarmResponseDto {

    private Long alarmId;
    private String alarmMessage;
    private String type;
    private Boolean isRead;
    private Date createdAt;
}
