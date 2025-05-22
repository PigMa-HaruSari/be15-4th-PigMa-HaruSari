package com.pigma.harusari.feedback.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FeedbackDetailDto {
    private Long feedbackId;
    private String content;
    private LocalDate date;
}
