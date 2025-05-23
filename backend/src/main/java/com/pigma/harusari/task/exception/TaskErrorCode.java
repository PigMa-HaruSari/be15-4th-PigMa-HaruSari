package com.pigma.harusari.task.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum TaskErrorCode {

    // 일정 관련 오류
    SCHEDULE_NOT_FOUND("20000", "일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    SCHEDULE_ALREADY_COMPLETED("20001", "이미 완료된 일정은 수정할 수 없습니다.", HttpStatus.CONFLICT),

    INVALID_SCHEDULE_DATE("20002", "과거 날짜로는 일정을 등록하실 수 없습니다.", HttpStatus.BAD_REQUEST),

    MISSING_SCHEDULE_DATE("20003", "일정 날짜가 입력되지 않았습니다.", HttpStatus.BAD_REQUEST),

    NEED_LOGIN("20004", "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),

    INVALID_MEMBER_INFO("20005", "회원 정보가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    INVALID_REQUEST("20998", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),

    ILLEGAL_ARGUMENT("20997", "잘못된 인자입니다.", HttpStatus.BAD_REQUEST),

    // 일정 자동화 관련 오류
    AUTOMATION_SCHEDULE_NOT_FOUND("21001", "자동화 일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    INVALID_REPEAT_TYPE("21002", "유효하지 않은 반복 유형입니다.", HttpStatus.BAD_REQUEST),

    INVALID_END_DATE("21003", "종료일이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    INVALID_REPEAT_WEEKDAYS("21004", "반복 요일 정보가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    INVALID_REPEAT_MONTHDAY("21005", "반복 월일 정보가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    CATEGORY_NOT_FOUND("21006", "카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    INVALID_DATE_FORMAT("21007", "날짜 형식이 잘못되었습니다. (예: 2025-00-00T00:00:00)", HttpStatus.BAD_REQUEST),

    REPEAT_TYPE_IS_NULL("21008", "[ERROR] RepeatType 값이 null입니다.", HttpStatus.BAD_REQUEST),

    REPEAT_TYPE_NOT_ALLOWED("21009", "[ERROR] 잘못된 RepeatType 값: ", HttpStatus.BAD_REQUEST),

    AUTOMATION_SCHEDULE_CREATE_FAILED("21010", "자동화 일정 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    UNAUTHORIZED_MODIFICATION("21011", "수정 권한이 없습니다.", HttpStatus.UNAUTHORIZED),

    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    private static final Map<String, TaskErrorCode> MESSAGE_TO_ENUM;

    static {
        MESSAGE_TO_ENUM = Arrays.stream(values())
                .collect(Collectors.toMap(TaskErrorCode::getMessage, e -> e));
    }

    public static Optional<TaskErrorCode> fromMessage(String message) {
        return Optional.ofNullable(MESSAGE_TO_ENUM.get(message));
    }

}
