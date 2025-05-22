package com.pigma.harusari.feedback.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FeedbackSummaryDto {
    private Long feedbackId;
    private String summary;      // 예: "2024년 4월 피드백"
    private LocalDate date;      // 피드백 날짜
}
