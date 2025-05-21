package com.pigma.harusari.common.oauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.oauth.dto.KakaoSignupRequest;
import com.pigma.harusari.common.oauth.dto.KakaoUserBasicInfo;
import com.pigma.harusari.common.oauth.service.KakaoAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KakaoAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[카카오 - controller] KakaoAuthController 테스트")
class KakaoAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoAuthService kakaoAuthService;

    @Autowired
    private ObjectMapper objectMapper;

    private KakaoUserBasicInfo kakaoUserBasicInfo;
    private KakaoSignupRequest kakaoSignupRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        kakaoUserBasicInfo = new KakaoUserBasicInfo("kakao@example.com", "카카오유저");

        kakaoSignupRequest = KakaoSignupRequest.builder()
                .email("kakao@example.com")
                .nickname("카카오유저")
                .consentPersonalInfo(true)
                .build();

        loginResponse = LoginResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .nickname("카카오유저")
                .userId(1L)
                .build();
    }

    @Test
    @DisplayName("[카카오 회원가입] 사용자 정보 조회 성공")
    void testGetKakaoUserInfo() throws Exception {
        String code = "fake-code";
        when(kakaoAuthService.getUserInfo(code)).thenReturn(kakaoUserBasicInfo);

        mockMvc.perform(get("/api/v1/auth/social/info/kakao")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(kakaoUserBasicInfo.getEmail()))
                .andExpect(jsonPath("$.data.nickname").value(kakaoUserBasicInfo.getNickname()));
    }

    @Test
    @DisplayName("[카카오 회원가입] 최종 회원가입 성공")
    void testSignupWithKakao() throws Exception {
        when(kakaoAuthService.signup(any(KakaoSignupRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/social/signup/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kakaoSignupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value(loginResponse.getAccessToken()))
                .andExpect(jsonPath("$.data.userId").value(loginResponse.getUserId()));
    }

    @Test
    @DisplayName("[카카오 로그인] 로그인 성공")
    void testLoginWithKakao() throws Exception {
        String code = "fake-login-code";
        when(kakaoAuthService.login(code)).thenReturn(loginResponse);

        mockMvc.perform(get("/api/v1/auth/social/login/kakao")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value(loginResponse.getAccessToken()))
                .andExpect(jsonPath("$.data.userId").value(loginResponse.getUserId()));
    }
}