package com.pigma.harusari.diary.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryUpdateRequest {

    @NotBlank(message = "회고 제목은 필수입니다.")
    private String diaryTitle;

    @NotBlank(message = "회고 내용은 필수입니다.")
    private String diaryContent;
}
