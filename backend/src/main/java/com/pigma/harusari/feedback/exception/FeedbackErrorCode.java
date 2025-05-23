package com.pigma.harusari.feedback.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedbackErrorCode {

    INSUFFICIENT_DATA("70001", "피드백을 생성할 수 없습니다: 지난달 일기와 스케줄이 각각 5개 미만입니다.", HttpStatus.BAD_REQUEST),
    GEMINI_API_ERROR("70002", "피드백 생성 중 AI 응답 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
