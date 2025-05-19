package com.pigma.harusari.alarm.command.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class AlarmCreateDto implements Serializable {
    private Long memberId;
    private String alarmMessage;
    private String type;
}
