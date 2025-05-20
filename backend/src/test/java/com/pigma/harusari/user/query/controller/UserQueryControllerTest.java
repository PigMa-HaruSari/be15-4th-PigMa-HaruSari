package com.pigma.harusari.user.query.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.common.auth.exception.AuthErrorCode;
import com.pigma.harusari.common.auth.exception.LogInMemberNotFoundException;
import com.pigma.harusari.user.query.dto.UserProfileResponse;
import com.pigma.harusari.user.query.service.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserQueryController.class)
@DisplayName("[회원 - controller] UserQueryController 테스트")
class UserQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserQueryService userQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[회원 정보 조회] 회원 정보 조회 성공")
    @WithMockUser(username = "1", roles = "USER")
    void testGetMyPage() throws Exception {
        // given
        UserProfileResponse mockResponse = UserProfileResponse.builder()
                .email("test@naver.com")
                .nickname("테스트유저")
                .gender("FEMALE")
                .userRegisteredAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        Mockito.when(userQueryService.getUserProfile(anyLong()))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/v1/users/mypage")
                        .header("Authorization", "Bearer fake-jwt-token")  // 실제 토큰 검증은 생략됨
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nickname").value("테스트유저"))
                .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                .andExpect(jsonPath("$.data.gender").value("FEMALE"));
    }

    @Test
    @DisplayName("[회원 정보 조회] 존재하지 않는 회원에게 예외를 발생하는 테스트")
    @WithMockUser(username = "999", roles = "USER")
    void testGetMyPageUserNotFound() throws Exception {
        // given
        Mockito.when(userQueryService.getUserProfile(anyLong()))
                .thenThrow(new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/v1/users/mypage")
                        .header("Authorization", "Bearer fake-jwt-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    @DisplayName("[회원 정보 조회] 인증되지 않은 사용자에 대해 예외를 발생하는 테스트")
    void testGetMyPageUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/users/mypage")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}