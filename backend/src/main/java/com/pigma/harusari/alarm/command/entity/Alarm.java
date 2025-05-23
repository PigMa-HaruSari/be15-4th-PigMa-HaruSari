package com.pigma.harusari.alarm.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "alarm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "alarm_message")
    private String alarmMessage;

    @Column(name = "type")
    private String type;

    @Column(name = "is_read")
    private Boolean isRead;
    
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "is_sent")
    private Boolean isSent = false;

    @Builder
    public Alarm(Long memberId, String alarmMessage, String type, Boolean isRead, Date createdAt, Boolean isSent) {
        this.memberId = memberId;
        this.alarmMessage = alarmMessage;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.isSent = isSent;
    }
}
