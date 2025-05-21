package com.pigma.harusari.common.oauth.service;

import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.jwt.JwtTokenProvider;
import com.pigma.harusari.common.oauth.dto.*;
import com.pigma.harusari.common.oauth.exception.*;
import com.pigma.harusari.user.command.entity.AuthProvider;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService {

    private final WebClient webClient;
    private final UserCommandRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

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

    /* 카카오 회원가입 - 최종 회원가입 처리(성별, 개인정보 동의 등) */
    @Override
    public LoginResponse signup(KakaoSignupRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new OAuthAlreadyRegisteredException(OAuthExceptionErrorCode.OAUTH_ALREADY_REGISTERED);
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password("")
                .gender(Gender.NONE)
                .consentPersonalInfo(request.getConsentPersonalInfo())
                .provider(AuthProvider.KAKAO)
                .build();

        Member saved;
        try {
            saved = memberRepository.save(member);
        } catch (Exception e) {
            throw new OAuthInternalErrorException(OAuthExceptionErrorCode.OAUTH_INTERNAL_ERROR);
        }

        return issueJwtTokens(saved);
    }

    /* 카카오 로그인 */
    @Override
    public LoginResponse login(String code) {
        String accessToken = requestAccessToken(code).getAccessToken();
        KakaoUserInfo kakaoUser = requestUserInfo(accessToken);

        String email = kakaoUser.getKakao_account().getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new OAuthUserNotFoundException(OAuthExceptionErrorCode.OAUTH_USER_NOT_FOUND));

        return issueJwtTokens(member);
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

    /* JWT 토큰 생성하고 리프레스 토큰을 Redis에 저장 */
    private LoginResponse issueJwtTokens(Member member) {
        try {
            String userId = String.valueOf(member.getMemberId());
            String accessToken = jwtTokenProvider.createToken(userId, member.getGender().name());
            String refreshToken = jwtTokenProvider.createRefreshToken(userId, member.getGender().name());

            try {
                redisTemplate.opsForValue().set(userId, refreshToken, Duration.ofDays(7));
            } catch (Exception e) {
                throw new OAuthRedisSaveFailedException(OAuthExceptionErrorCode.OAUTH_REDIS_SAVE_FAILED);
            }

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(member.getMemberId())
                    .nickname(member.getNickname())
                    .build();

        } catch (Exception e) {
            throw new OAuthJWTIssutFailedException(OAuthExceptionErrorCode.OAUTH_JWT_ISSUE_FAILED);
        }
    }

}
