package com.pigma.harusari.common.oauth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OAuthExceptionErrorCode {

    OAUTH_USER_INFO_INCOMPLETE("12001", "카카오 사용자 정보에 이메일 또는 닉네임이 없습니다.", HttpStatus.BAD_REQUEST),
    OAUTH_TOKEN_REQUEST_FAILED("12002", "카카오 토큰 요청 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
    OAUTH_INVALID_CODE("12003", "잘못되었거나 만료된 인가 코드입니다.", HttpStatus.BAD_REQUEST),
    OAUTH_USER_INFO_REQUEST_FAILED("12004", "카카오 사용자 정보를 가져오는 데 실패했습니다.", HttpStatus.BAD_GATEWAY),
    OAUTH_ALREADY_REGISTERED("12005", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT),
    OAUTH_REDIS_SAVE_FAILED("12006", "리프레시 토큰 저장 중 문제가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    OAUTH_JWT_ISSUE_FAILED("12007", "로그인 토큰 발급 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    OAUTH_USER_NOT_FOUND("12008", "가입되지 않은 카카오 사용자입니다.", HttpStatus.UNAUTHORIZED),
    OAUTH_INTERNAL_ERROR("12999", "소셜 인증 처리 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
