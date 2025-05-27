package com.pigma.harusari.task.schedule.command.repository;

import com.pigma.harusari.task.schedule.command.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository  extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByScheduleId(Long scheduleId);

    // 자동화 일정 ID와 기준일 이후의 일정 삭제 (수정!)
    void deleteByAutomationSchedule_AutomationScheduleIdAndScheduleDateAfter(Long automationScheduleId, LocalDate scheduleDate);

    List<Schedule> findByAutomationSchedule_AutomationScheduleIdAndScheduleDateAfter(Long automationScheduleId, LocalDate baseDate);

    Optional<Schedule> findFirstByAutomationSchedule_AutomationScheduleIdAndScheduleDateGreaterThanEqualOrderByScheduleDateAsc(
            Long automationScheduleId,
            LocalDate baseDate
    );

    void deleteByAutomationSchedule_AutomationScheduleId(Long scheduleId);

    void deleteByCategory_CategoryId(Long categoryId);

}
