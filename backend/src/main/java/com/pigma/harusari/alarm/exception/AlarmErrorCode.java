package com.pigma.harusari.alarm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AlarmErrorCode {

    SSE_CONNECTION_ERROR("40001", "SSE 연결 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SSE_SEND_ERROR("40002", "SSE 메시지 전송 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SSE_NOT_FOUND("40003", "SSE 연결이 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
