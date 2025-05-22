package com.pigma.harusari.user.command.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum UserCommandErrorCode {

    EMAIL_VERIFICATION_FAILED("10001", "인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_DUPLICATED("10002", "중복되는 이메일입니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_REQUIRED("10003", "닉네임을 입력해야 합니다.", HttpStatus.BAD_REQUEST),
    CONSENT_REQUIRED("10004", "개인 정보 수집 동의는 필수입니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_REQUIRED("10005", "1개 이상의 카테고리를 등록해야 합니다.", HttpStatus.BAD_REQUEST),
    EMPTY_UPDATE_REQUEST("10006", "수정할 정보가 없습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH("10007", "기존 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_MISMATCH("10008", "새 비밀번호와 확인 값이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_LENGTH_INVALID("10009", "비밀번호는 10자 이상 20자 이하로 입력해야 합니다.", HttpStatus.BAD_REQUEST),
    ALREADY_SIGNED_OUT_MEMBER("10010", "이미 탈퇴한 회원입니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND("10011", "등록되지 않은 회원입니다.", HttpStatus.BAD_REQUEST),
    INVALID_RESET_TOKEN("10012", "비밀번호 재설정 인증 토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    METHOD_ARG_NOT_VALID("10999", "@Valid 검증 오류입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    private static final Map<String, UserCommandErrorCode> MESSAGE_TO_ENUM;

    static {
        MESSAGE_TO_ENUM = Arrays.stream(values())
                .collect(Collectors.toMap(UserCommandErrorCode::getErrorMessage, e -> e));
    }

    public static Optional<UserCommandErrorCode> fromMessage(String message) {
        return Optional.ofNullable(MESSAGE_TO_ENUM.get(message));
    }

}