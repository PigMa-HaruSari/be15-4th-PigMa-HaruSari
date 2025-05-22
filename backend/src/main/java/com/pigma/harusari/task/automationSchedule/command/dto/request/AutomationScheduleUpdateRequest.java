package com.pigma.harusari.task.automationSchedule.command.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AutomationScheduleUpdateRequest {

    private Long categoryId;

    private String automationScheduleContent;

    @NotNull(message = "종료일은 필수입니다.")
    private LocalDate endDate;

    private String repeatType;

    private String repeatWeekdays;

    private Integer repeatMonthday;

    @AssertTrue(message = "WEEKLY일 때는 repeatWeekdays만, MONTHLY일 때는 repeatMonthday만 입력해야 합니다.")
    public boolean isValidRepeatFields() {
        if ("WEEKLY".equalsIgnoreCase(repeatType)) {
            return repeatWeekdays != null && !repeatWeekdays.isBlank() && repeatMonthday == null;
        }
        if ("MONTHLY".equalsIgnoreCase(repeatType)) {
            return repeatMonthday != null && (repeatWeekdays == null || repeatWeekdays.isBlank());
        }
        // DAILY 등 다른 타입은 둘 다 없어야 함
        return (repeatWeekdays == null || repeatWeekdays.isBlank()) && repeatMonthday == null;
    }

}