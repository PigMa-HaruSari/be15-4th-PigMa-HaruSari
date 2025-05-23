package com.pigma.harusari.alarm.command.repository;

import com.pigma.harusari.alarm.command.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Modifying
    @Query("UPDATE Alarm a SET a.isRead = true WHERE a.memberId = :memberId AND a.isRead = false")
    void markAllByMemberIdAsRead(Long memberId);

    @Modifying
    @Query("UPDATE Alarm a SET a.isSent = true WHERE a.alarmId = :alarmId")
    void markAsSent(Long alarmId);

    @Query("SELECT a FROM Alarm a WHERE a.memberId = :memberId AND a.isSent = false")
    List<Alarm> findUnsentAlarmsByMemberId(Long memberId);
}
