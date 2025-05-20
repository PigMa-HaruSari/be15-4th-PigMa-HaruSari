package com.pigma.harusari.common.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {

    LOGIN_MEMBER_NOT_FOUND("11001", "존재하지 않는 이메일입니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_PASSWORD_MISMATCH("11002", "이메일 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("11003", "리프레시 토큰이 유효하지 않거나 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_MEMBER_NOT_FOUND("11004", "해당 회원이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    END_OF_ERROR("11999", "", HttpStatus.BAD_REQUEST);


    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
