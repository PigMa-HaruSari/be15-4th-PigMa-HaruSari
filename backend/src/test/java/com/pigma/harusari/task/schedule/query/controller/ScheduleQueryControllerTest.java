package com.pigma.harusari.task.schedule.query.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.task.schedule.query.dto.response.ScheduleListResponse;
import com.pigma.harusari.task.schedule.query.service.ScheduleQueryService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[자동화 일정] ScheduleQueryController 테스트")
class ScheduleQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleQueryService scheduleQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    private void setAuthentication(Long memberId) {
        CustomUserDetails userDetails = Mockito.mock(CustomUserDetails.class);
        when(userDetails.getMemberId()).thenReturn(memberId);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("[일정 목록 조회] 성공")
    void testGetScheduleListSuccess() throws Exception {
        // given
        setAuthentication(10L);

        Long categoryId = 1L;
        LocalDate scheduleDate = LocalDate.of(2025, 6, 1);

        ScheduleListResponse response = ScheduleListResponse.builder()
                .schedule(List.of()) // 실제 테스트에서는 일정 DTO 리스트를 넣어도 됨
                .build();

        when(scheduleQueryService.getScheduleList(eq(categoryId), eq(scheduleDate), eq(10L)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/task/schedule")
                        .param("categoryId", String.valueOf(categoryId))
                        .param("scheduleDate", scheduleDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("[일정 목록 조회] 인증 정보 없음 - 예외 발생")
    void testGetScheduleListNeedLogin() throws Exception {
        // 인증 정보 세팅 안함

        mockMvc.perform(get("/api/v1/task/schedule")
                        .param("categoryId", "1")
                        .param("scheduleDate", "2025-06-01"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("20004"));

        SecurityContextHolder.clearContext();
    }
}
