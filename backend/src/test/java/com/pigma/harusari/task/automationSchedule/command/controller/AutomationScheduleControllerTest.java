package com.pigma.harusari.task.automationSchedule.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.task.automationSchedule.command.dto.request.AutomationScheduleCreateRequest;
import com.pigma.harusari.task.automationSchedule.command.service.AutomationScheduleService;
import com.pigma.harusari.user.command.entity.AuthProvider;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutomationScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[자동화 일정] AutomationScheduleController 테스트")
class AutomationScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutomationScheduleService automationScheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    AutomationScheduleCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = AutomationScheduleCreateRequest.builder()
                .categoryId(1L)
                .automationScheduleContent("자동화 내용")
                .endDate(LocalDate.of(2025, 6, 1))
                .repeatType("WEEKLY")
                .repeatWeekdays("MO,WE")
                .repeatMonthday(null)
                .build();
    }

    @Test
    @DisplayName("[자동화 일정 추가] 성공")
    void testCreateAutomationScheduleSuccess() throws Exception {
        // given
        Long expectedId = 100L;
        Long memberId = 10L;

        Member member = Member.builder()
                .email("user@email.com")
                .password("password")
                .nickname("테스트유저")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .provider(AuthProvider.LOCAL)
                .build();

        Member spyMember = spy(member);
        when(spyMember.getMemberId()).thenReturn(memberId);

        // CustomUserDetails 생성자에 Member 전달
        CustomUserDetails userDetails = new CustomUserDetails(spyMember);

        // SecurityContext에 인증 정보 세팅 (Spring Security에서 @AuthenticationPrincipal로 주입됨)
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(automationScheduleService.createAutomationSchedule(any(AutomationScheduleCreateRequest.class), eq(memberId)))
                .willReturn(expectedId);

        // when & then
        mockMvc.perform(post("/api/v1/task/automationschedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(expectedId));

        // 테스트 후 SecurityContext 초기화 (다른 테스트에 영향 방지)
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("[자동화 일정 수정] 성공")
    void testUpdateAutomationScheduleSuccess() throws Exception {
        // given
        Long scheduleId = 123L;
        Long memberId = 10L;

        Member member = Member.builder()
                .email("user@email.com")
                .password("password")
                .nickname("테스트유저")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .provider(AuthProvider.LOCAL)
                .build();

        Member spyMember = spy(member);
        when(spyMember.getMemberId()).thenReturn(memberId);

        CustomUserDetails userDetails = new CustomUserDetails(spyMember);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 서비스 Mock: 반환값 없는 void 메서드이므로 doNothing().when... 또는 그냥 생략 가능
        // doNothing().when(automationScheduleService).updateAutomationSchedule(eq(scheduleId), any(AutomationScheduleCreateRequest.class), eq(memberId));

        // when & then
        mockMvc.perform(put("/api/v1/task/automationschedules/{id}", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist());

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("[자동화 일정 삭제] 성공")
    void testDeleteAutomationScheduleSuccess() throws Exception {
        // given
        Long scheduleId = 123L;
        Long memberId = 10L;

        Member member = Member.builder()
                .email("user@email.com")
                .password("password")
                .nickname("테스트유저")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .provider(AuthProvider.LOCAL)
                .build();

        Member spyMember = spy(member);
        when(spyMember.getMemberId()).thenReturn(memberId);

        CustomUserDetails userDetails = new CustomUserDetails(spyMember);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 서비스 계층 void 메서드이므로 특별한 mock 설정 없이 진행 가능

        // when & then
        mockMvc.perform(delete("/api/v1/task/automationschedules/{id}", scheduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist());

        SecurityContextHolder.clearContext();
    }
}