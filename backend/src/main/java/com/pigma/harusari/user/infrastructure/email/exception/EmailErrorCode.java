package com.pigma.harusari.user.infrastructure.email.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode {

    SIGNUP_EMAIL_CODE_INVALID("13001", "인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    RESET_EMAIL_CODE_INVALID("13002", "인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    private static final Map<String, EmailErrorCode> MESSAGE_TO_ENUM;

    static {
        MESSAGE_TO_ENUM = Arrays.stream(values())
                .collect(Collectors.toMap(EmailErrorCode::getErrorMessage, e -> e));
    }

    public static Optional<EmailErrorCode> fromMessage(String message) {
        return Optional.ofNullable(MESSAGE_TO_ENUM.get(message));
    }

}