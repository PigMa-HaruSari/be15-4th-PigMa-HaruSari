package com.pigma.harusari.diary.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DiaryResponseDto {
    private final Long diaryId; // ✅ 추가
    private final String diaryTitle;
    private final String diaryContent;
    private final LocalDateTime createdAt; // ✅ 추가
}
