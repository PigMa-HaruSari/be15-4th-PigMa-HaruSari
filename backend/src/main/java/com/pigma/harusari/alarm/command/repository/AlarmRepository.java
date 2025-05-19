package com.pigma.harusari.alarm.command.repository;

import com.pigma.harusari.alarm.command.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
