package com.pigma.harusari.task.schedule.command.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequest {

    private Long automationScheduleId;

    @NotNull(message = "카테고리를 선택해 주세요.")
    private Long categoryId;

    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(min = 2, max = 50, message = "내용은 2~50자 이내로 입력해 주세요.")
    private String scheduleContent;

    @NotNull(message = "일정 날짜는 필수입니다.")
    @FutureOrPresent(message = "일정 날짜는 오늘 이후여야 합니다.")
    private LocalDate scheduleDate;

    @Builder
    public ScheduleCreateRequest(
            Long automationScheduleId, Long categoryId, String scheduleContent, LocalDate scheduleDate
    ) {
        this.automationScheduleId = automationScheduleId;
        this.categoryId = categoryId;
        this.scheduleContent = scheduleContent;
        this.scheduleDate = scheduleDate;
    }



}
