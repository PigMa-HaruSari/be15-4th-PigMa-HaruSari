package com.pigma.harusari.task.schedule.command.entity;

import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.task.automationSchedule.command.entity.AutomationSchedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "automation_schedule_id")
    private AutomationSchedule automationSchedule;

    @Column(name = "schedule_content")
    private String scheduleContent;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "completion_status", nullable = false)
    private Boolean completionStatus = false;

    @Builder
    public Schedule(
            Category category, AutomationSchedule automationSchedule, String scheduleContent,
            LocalDate scheduleDate, LocalDateTime createdAt, LocalDateTime modifiedAt, Boolean completionStatus
    ) {
        this.category = category;
        this.automationSchedule = automationSchedule;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.completionStatus = completionStatus;
    }

    public void setCompletionStatus(Boolean completionStatus) {
        this.completionStatus =(completionStatus != null) ? completionStatus : false;
    }

    public void setScheduleContent(@NotBlank String scheduleContent) {
        this.scheduleContent = scheduleContent;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setAutomationSchedule(AutomationSchedule automationSchedule) { this.automationSchedule = automationSchedule; }


}
