package com.pigma.harusari.alarm.command.service;

public interface AlarmCommandService {

    void markAllAsRead(Long memberId);

    void sendUnsentAlarms(Long memberId);
}
