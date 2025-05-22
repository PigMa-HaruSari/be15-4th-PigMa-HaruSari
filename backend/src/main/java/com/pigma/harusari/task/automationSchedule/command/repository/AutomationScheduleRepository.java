package com.pigma.harusari.task.automationSchedule.command.repository;

import com.pigma.harusari.task.automationSchedule.command.entity.AutomationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AutomationScheduleRepository extends JpaRepository<AutomationSchedule, Long> {
        List<AutomationSchedule> findByEndDateAfter(LocalDate date);


}
