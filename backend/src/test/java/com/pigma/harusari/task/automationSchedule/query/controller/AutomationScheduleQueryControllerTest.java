package com.pigma.harusari.task.automationSchedule.query.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.task.automationSchedule.command.controller.AutomationScheduleController;
import com.pigma.harusari.task.automationSchedule.query.dto.request.AutomationScheduleRequest;
import com.pigma.harusari.task.automationSchedule.query.dto.response.AutomationScheduleDto;
import com.pigma.harusari.task.automationSchedule.query.service.AutomationScheduleQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutomationScheduleQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[자동화 일정] AutomationScheduleQueryController 테스트")
class AutomationScheduleQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutomationScheduleQueryService automationScheduleQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[자동화 일정 목록 조회] 성공")
    void testGetAutomationSchedulesSuccess() throws Exception {
        // given
        Long memberId = 10L;

        // 인증 정보 세팅
        CustomUserDetails userDetails = Mockito.mock(CustomUserDetails.class);
        when(userDetails.getMemberId()).thenReturn(memberId);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 서비스 Mock 반환값
        List<AutomationScheduleDto> expectedList = List.of(
                AutomationScheduleDto.builder()
                        .automationScheduleId(1L)
                        .categoryId(1L)
                        .automationScheduleContent("테스트 자동화")
                        .repeatType("MONTHLY")
                        .build()
        );
        when(automationScheduleQueryService.getAutomationScheduleList(any(AutomationScheduleRequest.class), eq(memberId)))
                .thenReturn(expectedList);

        // when & then
        mockMvc.perform(get("/api/v1/task/automationschedules")
                        .param("categoryId", "1")
                        .param("repeatType", "MONTHLY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].automationScheduleId").value(1L))
                .andExpect(jsonPath("$[0].categoryId").value(1L))
                .andExpect(jsonPath("$[0].repeatType").value("MONTHLY"))
                .andExpect(jsonPath("$[0].automationScheduleContent").value("테스트 자동화"));

        // 인증 정보 해제
        SecurityContextHolder.clearContext();
    }


    @Test
    @DisplayName("[가장 가까운 자동화 일정 조회] 성공")
    void testGetNearestScheduleSuccess() throws Exception {
        // given
        Long memberId = 10L;
        Long automationScheduleId = 1L;

        // 인증 정보 세팅
        CustomUserDetails userDetails = Mockito.mock(CustomUserDetails.class);
        when(userDetails.getMemberId()).thenReturn(memberId);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 서비스 Mock 반환값
        AutomationScheduleDto dto = AutomationScheduleDto.builder()
                .automationScheduleId(1L)
                .categoryId(2L)
                .automationScheduleContent("가장 가까운 자동화 일정")
                .repeatType("MONTHLY")
                .build();

        when(automationScheduleQueryService.getNearestAutomationSchedule(eq(automationScheduleId), eq(memberId)))
                .thenReturn(dto);

        // when & then
        mockMvc.perform(get("/api/v1/task/automationschedules/nearest")
                        .param("automationScheduleId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.automationScheduleId").value(1L))
                .andExpect(jsonPath("$.data.categoryId").value(2L))
                .andExpect(jsonPath("$.data.repeatType").value("MONTHLY"))
                .andExpect(jsonPath("$.data.automationScheduleContent").value("가장 가까운 자동화 일정"));

        // 인증 정보 해제
        SecurityContextHolder.clearContext();
    }
}
