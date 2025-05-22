package com.pigma.harusari.task.automationSchedule.command.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AutomationScheduleCreateRequest {

    @NotNull(message = "카테고리를 선택해 주세요.")
    private Long categoryId;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String automationScheduleContent;

    @NotBlank(message = "종료일을 입력해 주세요.")
    private String endDate;

    @NotBlank(message = "반복 유형을 입력해 주세요.")
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