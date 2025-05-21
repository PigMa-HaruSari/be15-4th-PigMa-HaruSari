package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OAuthExceptionErrorCode {

    OAUTH_USER_INFO_INCOMPLETE("12004", "카카오 사용자 정보에 이메일 또는 닉네임이 없습니다.", HttpStatus.BAD_REQUEST),
    OAUTH_TOKEN_REQUEST_FAILED("12002", "카카오 토큰 요청 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
    OAUTH_INVALID_CODE("12001", "잘못되었거나 만료된 인가 코드입니다.", HttpStatus.BAD_REQUEST),
    OAUTH_USER_INFO_REQUEST_FAILED("12003", "카카오 사용자 정보를 가져오는 데 실패했습니다.", HttpStatus.BAD_GATEWAY),;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
