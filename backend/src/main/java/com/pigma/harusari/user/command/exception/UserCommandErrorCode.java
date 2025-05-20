package com.pigma.harusari.user.command.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserCommandErrorCode {

    EMAIL_VERIFICATION_FAILED("10001", "인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_DUPLICATED("10002", "중복되는 이메일입니다.", HttpStatus.BAD_REQUEST),
    NICKNAME_REQUIRED("10003", "닉네임을 입력해야 합니다.", HttpStatus.BAD_REQUEST),
    CATEGORY_REQUIRED("10004", "1개 이상의 카테고리를 등록해야 합니다.", HttpStatus.BAD_REQUEST),
    END_OF_ERROR("19999", "", HttpStatus.BAD_REQUEST);


    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
