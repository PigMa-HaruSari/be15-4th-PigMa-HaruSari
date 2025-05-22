package com.pigma.harusari.common.auth.service;

import com.pigma.harusari.common.auth.dto.LoginRequest;
import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.auth.dto.TokenResponse;
import com.pigma.harusari.common.auth.entity.RefreshToken;
import com.pigma.harusari.common.auth.exception.*;
import com.pigma.harusari.common.jwt.JwtTokenProvider;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[인증 - service] AuthServiceImpl 테스트")
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserCommandRepository userCommandRepository;

    @Mock
    private RedisTemplate<String, RefreshToken> redisTemplate;

    @Mock
    private ValueOperations<String, RefreshToken> valueOperations;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[로그인] 로그인 성공 테스트")
    void testLoginSuccess() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        Member member = Member.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .nickname("성이름")
                .gender(Gender.FEMALE)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);

        given(userCommandRepository.findByEmail("test@example.com"))
                .willReturn(Optional.of(member));
        given(passwordEncoder.matches("password123", "encodedPassword"))
                .willReturn(true);
        given(jwtTokenProvider.createToken(1L, "FEMALE"))
                .willReturn("access-token");
        given(jwtTokenProvider.createRefreshToken(1L, "FEMALE"))
                .willReturn("refresh-token");
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        // when
        LoginResponse response = authService.login(request);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getNickname()).isEqualTo("성이름");
        assertThat(response.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("[로그인] 존재하지 않는 이메일로 로그인 시 예외 발생")
    void testLoginMemberNotFound() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        given(userCommandRepository.findByEmail("test@example.com"))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(LogInMemberNotFoundException.class)
                .hasMessage(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("[로그인] 비밀번호 불일치 시 예외 발생")
    void testLoginPasswordMismatch() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");
        Member member = Member.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .nickname("성이름")
                .gender(Gender.FEMALE)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        given(userCommandRepository.findByEmail("test@example.com"))
                .willReturn(Optional.of(member));
        given(passwordEncoder.matches("wrongpassword", "encodedPassword"))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(LogInPasswordMismatchException.class)
                .hasMessage(AuthErrorCode.LOGIN_PASSWORD_MISMATCH.getErrorMessage());
    }

    @Test
    @DisplayName("[리프레시] 리프레시 토큰 불일치 시 예외 발생")
    void testRefreshTokenInvalid() {
        // given
        String refreshToken = "invalid-token";
        given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
        given(jwtTokenProvider.getUsernameFromJWT(refreshToken)).willReturn("1");
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("1")).willReturn(RefreshToken.builder().token("different-token").build());

        // when & then
        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(RefreshTokenInvalidException.class)
                .hasMessage(AuthErrorCode.REFRESH_TOKEN_INVALID.getErrorMessage());
    }

    @Test
    @DisplayName("[리프레시] 존재하지 않는 회원으로 리프레시 시 예외 발생")
    void testRefreshTokenUserNotFound() {
        // given
        String refreshToken = "refresh-token";
        given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
        given(jwtTokenProvider.getUsernameFromJWT(refreshToken)).willReturn("1");
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("1")).willReturn(RefreshToken.builder().token("refresh-token").build());
        given(userCommandRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(RefreshMemberNotFoundException.class)
                .hasMessage(AuthErrorCode.REFRESH_MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("[리프레시] 리프레시 성공 테스트")
    void testRefreshTokenSuccess() {
        // given
        String oldToken = "refresh-token";
        Member member = Member.builder()
                .email("test@example.com")
                .password("encoded")
                .nickname("성이름")
                .gender(Gender.FEMALE)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);

        given(jwtTokenProvider.validateToken(oldToken)).willReturn(true);
        given(jwtTokenProvider.getUsernameFromJWT(oldToken)).willReturn("1");
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("1")).willReturn(RefreshToken.builder().token(oldToken).build());
        given(userCommandRepository.findById(1L)).willReturn(Optional.of(member));
        given(jwtTokenProvider.createToken("1", "FEMALE")).willReturn("new-access-token");
        given(jwtTokenProvider.createRefreshToken("1", "FEMALE")).willReturn("new-refresh-token");

        // when
        TokenResponse response = authService.refreshToken(oldToken);

        // then
        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");
    }

    @Test
    @DisplayName("[로그아웃] 리프레시 토큰이 있는 경우 로그아웃 처리")
    void testLogoutWithToken() {
        // given
        String refreshToken = "refresh-token";
        given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
        given(jwtTokenProvider.getUsernameFromJWT(refreshToken)).willReturn("1");

        // when
        authService.logout(refreshToken);

        // then
        verify(redisTemplate).delete("1");
    }

    @Test
    @DisplayName("[로그아웃] 리프레시 토큰이 없는 경우 로그아웃 정상 처리")
    void testLogoutWithoutToken() {
        // when
        authService.logout(null);

        // then
        // 검증 과정 없이 정상 종료
    }
}
