package com.pigma.harusari.feedback.query.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.YearMonth;

@Getter
public class FeedbackSummaryDto {
    private final Long feedbackId;
    private final String summary;
    private final LocalDate date;

    public FeedbackSummaryDto(Long feedbackId, LocalDate date) {
        this.feedbackId = feedbackId;
        this.date = date;

        // "피드백 생성일의 전달"을 기준으로 summary 구성
        YearMonth ym = YearMonth.from(date).minusMonths(1);
        this.summary = String.format("%d년 %d월 피드백", ym.getYear(), ym.getMonthValue());
    }
}
