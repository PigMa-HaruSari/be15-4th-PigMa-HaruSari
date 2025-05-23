package com.pigma.harusari.diary.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiaryResponseDto {

    private final String diaryTitle;
    private final String diaryContent;
}
