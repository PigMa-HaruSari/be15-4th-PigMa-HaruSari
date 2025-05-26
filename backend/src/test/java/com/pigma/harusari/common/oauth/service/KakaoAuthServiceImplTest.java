package com.pigma.harusari.common.oauth.service;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.jwt.JwtTokenProvider;
import com.pigma.harusari.common.oauth.dto.KakaoSignupRequest;
import com.pigma.harusari.common.oauth.dto.KakaoTokenResponse;
import com.pigma.harusari.common.oauth.dto.KakaoUserBasicInfo;
import com.pigma.harusari.common.oauth.dto.KakaoUserInfo;
import com.pigma.harusari.common.oauth.exception.*;
import com.pigma.harusari.user.command.entity.AuthProvider;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("[카카오 - service] KakaoAuthServiceImpl 테스트")
class KakaoAuthServiceImplTest {

    @Spy
    @InjectMocks
    private KakaoAuthServiceImpl kakaoAuthServiceImpl;

    @Mock
    private UserCommandRepository memberRepository;

    @Mock
    private CategoryCommandRepository categoryRepository;

    @Mock private WebClient webClient;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private RedisTemplate<String, Object> redisTemplate;

    private final String fakeCode = "fake-code";
    private final String fakeAccessToken = "access-token";
    private final String email = "kakao@example.com";
    private final String nickname = "카카오사용자";
    private final String redirectUri = "http://localhost:3000";

    private Member member;
    private LoginResponse loginResponse;
    private KakaoSignupRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password("")
                .gender(Gender.NONE)
                .consentPersonalInfo(true)
                .provider(AuthProvider.KAKAO)
                .build();

        loginResponse = LoginResponse.builder()
                .userId(1L)
                .nickname(nickname)
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        request = KakaoSignupRequest.builder()
                .email(email)
                .nickname(nickname)
                .consentPersonalInfo(true)
                .gender("NONE")
                .categoryList(List.of(CategoryCreateRequest.builder()
                        .categoryName("취업")
                        .color("#000000")
                        .build()))
                .build();

        ReflectionTestUtils.setField(member, "memberId", 1L);
        ReflectionTestUtils.setField(kakaoAuthServiceImpl, "signupRedirectUri", redirectUri);
        ReflectionTestUtils.setField(kakaoAuthServiceImpl, "loginRedirectUri", redirectUri);
    }

    @Test
    @DisplayName("[카카오 회원가입 - 사용자 조회] 정상 정보 반환")
    void testGetUserInfoSuccess() {

        KakaoTokenResponse tokenResponse = KakaoTokenResponse.builder().accessToken(fakeAccessToken).build();
        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                .id(123L)
                .kakao_account(KakaoUserInfo.KakaoAccount.builder()
                        .email(email)
                        .profile(KakaoUserInfo.KakaoAccount.Profile.builder().nickname(nickname).build())

                        .build())
                .build();

        doReturn(tokenResponse).when(kakaoAuthServiceImpl).requestAccessToken(fakeCode, redirectUri);
        doReturn(kakaoUserInfo).when(kakaoAuthServiceImpl).requestUserInfo(fakeAccessToken);

        KakaoUserBasicInfo info = kakaoAuthServiceImpl.getUserInfo(fakeCode);

        assertThat(info.getEmail()).isEqualTo(email);
        assertThat(info.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("[카카오 회원가입 - 사용자 조회] 사용자 정보 누락 예외")
    void testGetUserInfoMissingData() {
        KakaoTokenResponse tokenResponse = KakaoTokenResponse.builder().accessToken(fakeAccessToken).build();
        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                .id(123L)
                .kakao_account(KakaoUserInfo.KakaoAccount.builder()
                        .email(null)
                        .profile(KakaoUserInfo.KakaoAccount.Profile.builder().nickname(null).build())
                        .build())
                .build();

        doReturn(tokenResponse).when(kakaoAuthServiceImpl).requestAccessToken(fakeCode, redirectUri);
        doReturn(kakaoUserInfo).when(kakaoAuthServiceImpl).requestUserInfo(fakeAccessToken);

        assertThatThrownBy(() -> kakaoAuthServiceImpl.getUserInfo(fakeCode))
                .isInstanceOf(OAuthUserInfoIncompleteException.class)
                .hasMessage(OAuthExceptionErrorCode.OAUTH_USER_INFO_INCOMPLETE.getErrorMessage());
    }


    @Test
    @DisplayName("[카카오 회원가입 - 최종 가입] 성공 테스트")
    void testSignupSuccess() {
        // given
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());
        given(categoryRepository.save(any())).willReturn(null);

        // memberId 강제로 삽입
        given(memberRepository.save(any())).willAnswer(invocation -> {
            Member input = invocation.getArgument(0);
            ReflectionTestUtils.setField(input, "memberId", 1L);
            return input;
        });

        doReturn(loginResponse).when(kakaoAuthServiceImpl).issueJwtTokens(any(Member.class));

        // when
        LoginResponse actual = kakaoAuthServiceImpl.signup(request);

        // then
        assertThat(actual.getNickname()).isEqualTo(loginResponse.getNickname());
        assertThat(actual.getUserId()).isEqualTo(loginResponse.getUserId());
    }

    @Test
    @DisplayName("[카카오 회원가입 - 최종 가입] 이미 가입된 사용자 예외")
    void testSignupAlreadyExists() {
        // given
        KakaoSignupRequest request = KakaoSignupRequest.builder()
                .email(email)
                .nickname(nickname)
                .consentPersonalInfo(true)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(Member.builder().email(email).build()));

        // when & then
        assertThatThrownBy(() -> kakaoAuthServiceImpl.signup(request))
                .isInstanceOf(OAuthAlreadyRegisteredException.class)
                .hasMessage(OAuthExceptionErrorCode.OAUTH_ALREADY_REGISTERED.getErrorMessage());
    }

    @Test
    @DisplayName("[카카오 회원가입 - 최종 가입] 내부 저장 중 오류 발생")
    void testSignupInternalSaveError() {
        // given
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());
        given(memberRepository.save(any())).willThrow(new RuntimeException("DB 에러"));

        // when & then
        assertThatThrownBy(() -> kakaoAuthServiceImpl.signup(request))
                .isInstanceOf(OAuthInternalErrorException.class)
                .hasMessage(OAuthExceptionErrorCode.OAUTH_INTERNAL_ERROR.getErrorMessage());
    }

    @Test
    @DisplayName("[카카오 로그인] 성공 테스트")
    void testLoginSuccess() {
        // given
        KakaoTokenResponse tokenResponse = KakaoTokenResponse.builder()
                .accessToken(fakeAccessToken)
                .build();

        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                .id(123L)
                .kakao_account(KakaoUserInfo.KakaoAccount.builder()
                        .email(email)
                        .profile(KakaoUserInfo.KakaoAccount.Profile.builder()
                                .nickname(nickname)
                                .build())
                        .build())
                .build();

        LoginResponse expectedResponse = LoginResponse.builder()
                .accessToken("jwtAccessToken")
                .refreshToken("jwtRefreshToken")
                .userId(1L)
                .nickname(nickname)
                .build();

        doReturn(tokenResponse).when(kakaoAuthServiceImpl).requestAccessToken(fakeCode, redirectUri);
        doReturn(kakaoUserInfo).when(kakaoAuthServiceImpl).requestUserInfo(fakeAccessToken);
        doReturn(expectedResponse).when(kakaoAuthServiceImpl).issueJwtTokens(member);
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        // when
        LoginResponse actual = kakaoAuthServiceImpl.login(fakeCode);

        // then
        assertThat(actual.getAccessToken()).isEqualTo("jwtAccessToken");
        assertThat(actual.getUserId()).isEqualTo(1L);
        assertThat(actual.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("[카카오 로그인] 이메일 기반 사용자 없을 시 예외 테스트")
    void testLoginUserNotFound() {
        KakaoTokenResponse tokenResponse = KakaoTokenResponse.builder()
                .accessToken(fakeAccessToken)
                .build();

        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                .id(123L)
                .kakao_account(KakaoUserInfo.KakaoAccount.builder()
                        .email(email)
                        .profile(KakaoUserInfo.KakaoAccount.Profile.builder()
                                .nickname(nickname)
                                .build())
                        .build())
                .build();

        doReturn(tokenResponse).when(kakaoAuthServiceImpl).requestAccessToken(fakeCode, redirectUri);
        doReturn(kakaoUserInfo).when(kakaoAuthServiceImpl).requestUserInfo(fakeAccessToken);
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() -> kakaoAuthServiceImpl.login(fakeCode))
                .isInstanceOf(OAuthUserNotFoundException.class)
                .hasMessage(OAuthExceptionErrorCode.OAUTH_USER_NOT_FOUND.getErrorMessage());
    }

}
