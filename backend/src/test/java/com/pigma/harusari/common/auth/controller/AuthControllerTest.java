package com.pigma.harusari.common.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.common.auth.dto.LoginRequest;
import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.auth.dto.TokenResponse;
import com.pigma.harusari.common.auth.exception.*;
import com.pigma.harusari.common.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("[인증 - controller] AuthController 테스트")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    LoginRequest loginRequest;
    LoginResponse loginResponse;
    TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        loginResponse = LoginResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .nickname("성이름")
                .userId(1L)
                .build();

        tokenResponse = TokenResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .build();
    }

    @Test
    @DisplayName("[로그인] 로그인 요청 성공 테스트")
    void testLoginSuccess() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE));
    }

    @Test
    @DisplayName("[로그인] 존재하지 않는 이메일로 로그인 시 예외 발생")
    void testLoginMemberNotFound() throws Exception {
        doThrow(new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND)).when(authService).login(any());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    @DisplayName("[로그인] 비밀번호 불일치 시 예외 발생")
    void testLoginPasswordMismatch() throws Exception {
        doThrow(new LogInPasswordMismatchException(AuthErrorCode.LOGIN_PASSWORD_MISMATCH)).when(authService).login(any());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(AuthErrorCode.LOGIN_PASSWORD_MISMATCH.getErrorCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.LOGIN_PASSWORD_MISMATCH.getErrorMessage()));
    }

    @Test
    @DisplayName("[리프레시] 리프레시 성공 테스트")
    void testRefreshTokenSuccess() throws Exception {
        when(authService.refreshToken("refresh-token")).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(new Cookie("refreshToken", "refresh-token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("new-refresh-token"))
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE));
    }

    @Test
    @DisplayName("[리프레시] 리프레시 토큰이 없는 경우 예외 발생")
    void testRefreshTokenMissing() throws Exception {
        mockMvc.perform(post("/api/v1/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[리프레시] 리프레시 토큰 불일치 시 예외 발생")
    void testRefreshTokenInvalid() throws Exception {
        doThrow(new RefreshTokenInvalidException(AuthErrorCode.REFRESH_TOKEN_INVALID)).when(authService).refreshToken("refresh-token");

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(new Cookie("refreshToken", "refresh-token")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(AuthErrorCode.REFRESH_TOKEN_INVALID.getErrorCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.REFRESH_TOKEN_INVALID.getErrorMessage()));
    }

    @Test
    @DisplayName("[리프레시] 존재하지 않는 회원으로 리프레시 시 예외 발생")
    void testRefreshTokenUserNotFound() throws Exception {
        doThrow(new RefreshMemberNotFoundException(AuthErrorCode.REFRESH_MEMBER_NOT_FOUND)).when(authService).refreshToken("refresh-token");

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(new Cookie("refreshToken", "refresh-token")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(AuthErrorCode.REFRESH_MEMBER_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.REFRESH_MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    @DisplayName("[로그아웃] 리프레시 토큰이 있는 경우 로그아웃 및 쿠키 삭제")
    void testLogoutWithRefreshToken() throws Exception {
        doNothing().when(authService).logout("refresh-token");

        mockMvc.perform(post("/api/v1/auth/logout")
                        .cookie(new Cookie("refreshToken", "refresh-token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, org.hamcrest.Matchers.containsString("Max-Age=0")));
    }

    @Test
    @DisplayName("[로그아웃] 리프레시 토큰 없이도 로그아웃 처리")
    void testLogoutWithoutRefreshToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, org.hamcrest.Matchers.containsString("Max-Age=0")));
    }

}
