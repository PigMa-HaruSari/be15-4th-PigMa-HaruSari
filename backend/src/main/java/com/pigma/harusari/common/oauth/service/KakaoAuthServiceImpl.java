package com.pigma.harusari.common.oauth.service;

import com.pigma.harusari.common.oauth.dto.*;
import com.pigma.harusari.common.oauth.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService {

    private final WebClient webClient;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    /* 카카오 회원가입 - 사용자 정보만 조회(실제 가입은 X) */
    @Override
    public KakaoUserBasicInfo getUserInfo(String code) {
        String accessToken = requestAccessToken(code).getAccessToken();
        KakaoUserInfo kakaoUser = requestUserInfo(accessToken);

        String email = kakaoUser.getKakao_account().getEmail();
        String nickname = kakaoUser.getKakao_account().getProfile().getNickname();

        if (email == null || nickname == null) {
            throw new OAuthUserInfoIncompleteException(OAuthExceptionErrorCode.OAUTH_USER_INFO_INCOMPLETE);
        }
        return new KakaoUserBasicInfo(email, nickname);
    }

    /* 카카오 인가 코드 기반으로 토큰 요청 */
    private KakaoTokenResponse requestAccessToken(String code) {
        try {
            return webClient.post()
                    .uri(tokenUri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters
                            .fromFormData("grant_type", "authorization_code")
                            .with("client_id", clientId)
                            .with("redirect_uri", redirectUri)
                            .with("code", code))
                    .retrieve()
                    .bodyToMono(KakaoTokenResponse.class)
                    .blockOptional()
                    .orElseThrow(() -> new OAuthTokenRequestFailedException(OAuthExceptionErrorCode.OAUTH_TOKEN_REQUEST_FAILED));
        } catch (Exception e) {
            throw new OAuthInvalidCodeException(OAuthExceptionErrorCode.OAUTH_INVALID_CODE);
        }
    }

    /* AccessToken 기반으로 카카오 사용자의 정보 조회 */
    private KakaoUserInfo requestUserInfo(String accessToken) {
        try {
            return webClient.get()
                    .uri(userInfoUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(KakaoUserInfo.class)
                    .blockOptional()
                    .orElseThrow(() -> new OAuthUserInfoRequestFailedException(OAuthExceptionErrorCode.OAUTH_USER_INFO_REQUEST_FAILED));
        } catch (Exception e) {
            throw new OAuthUserInfoRequestFailedException(OAuthExceptionErrorCode.OAUTH_USER_INFO_REQUEST_FAILED);
        }
    }

}
