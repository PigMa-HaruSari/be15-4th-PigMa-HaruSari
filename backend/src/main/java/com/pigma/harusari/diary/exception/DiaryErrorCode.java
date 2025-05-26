package com.pigma.harusari.diary.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DiaryErrorCode {

    DUPLICATE_DIARY("60001", "오늘은 이미 회고를 작성하셨습니다.", HttpStatus.BAD_REQUEST),
    DIARY_NOT_FOUND("60002", "회고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_MODIFICATION("60003", "작성 당일에만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DELETION("60004", "작성 당일에만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
