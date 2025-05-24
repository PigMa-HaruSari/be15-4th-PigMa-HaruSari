package com.pigma.harusari.category.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode {

    DUPLICATE_CATEGORY("30001", "이미 동일한 이름의 카테고리가 존재합니다.", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND("30002", "존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),
    CATEGORY_ACCESS_DENIED("30003", "해당 카테고리에 접근할 수 없습니다.", HttpStatus.FORBIDDEN),
    CANNOT_DELETE_CATEGORY_WITH_SCHEDULES("30004", "일정이 포함된 카테고리는 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CONFIRMATION_TEXT_MISMATCH("30005", "삭제 확인 문구가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
