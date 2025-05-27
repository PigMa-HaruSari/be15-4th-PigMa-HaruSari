package com.pigma.harusari.task.automationSchedule.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "automation_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class AutomationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "automation_schedule_id")
    private Long automationScheduleId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "automation_schedule_content")
    private String automationScheduleContent;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_type")
    private RepeatType repeatType;

    @Column(name = "repeat_weekdays")
    private String repeatWeekdays;

    @Column(name = "repeat_monthday")
    private Integer repeatMonthday;

    @Column(name = "deleted_flag", nullable = false)
    private boolean deletedFlag = false;

    @Builder
    public AutomationSchedule(
            Long categoryId, String automationScheduleContent, LocalDate endDate,
            RepeatType repeatType, String repeatWeekdays, Integer repeatMonthday
    ) {
        this.categoryId = categoryId;
        this.automationScheduleContent = automationScheduleContent;
        this.endDate = endDate;
        this.repeatType = repeatType;
        this.repeatWeekdays = repeatWeekdays;
        this.repeatMonthday = repeatMonthday;
    }

    public void updateCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void updateContent(String content) { this.automationScheduleContent = content; }
    public void updateEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void updateRepeatType(RepeatType repeatType) { this.repeatType = repeatType; }
    public void updateRepeatWeekdays(String weekdays) { this.repeatWeekdays = weekdays; }
    public void updateRepeatMonthday(Integer monthday) { this.repeatMonthday = monthday; }
    public void softDelete() {this.deletedFlag = true; }
}