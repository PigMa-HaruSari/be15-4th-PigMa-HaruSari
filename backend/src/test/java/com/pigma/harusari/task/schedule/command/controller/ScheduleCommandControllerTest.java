package com.pigma.harusari.task.schedule.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.task.schedule.command.dto.request.CompletionStatusUpdateRequest;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleCreateRequest;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleUpdateRequest;
import com.pigma.harusari.task.schedule.command.dto.response.ScheduleCommandResponse;
import com.pigma.harusari.task.schedule.command.service.ScheduleCommandService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[일정] ScheduleCommandController 테스트")
class ScheduleCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleCommandService scheduleCommandService;

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
    @DisplayName("[일정 생성] 성공")
    void testCreateScheduleSuccess() throws Exception {
        // given
        setAuthentication(10L);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .categoryId(1L)
                .scheduleContent("테스트 일정")
                .scheduleDate(LocalDate.of(2025, 6, 1))
                .build();

        when(scheduleCommandService.createSchedule(any(ScheduleCreateRequest.class), eq(10L)))
                .thenReturn(100L);

        // when & then
        mockMvc.perform(post("/api/v1/task/schedule")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(100L));

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("[일정 수정] 성공")
    void testUpdateScheduleSuccess() throws Exception {
        // given
        setAuthentication(10L);

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
                .categoryId(1L)
                .scheduleContent("수정된 일정")
                .scheduleDate(LocalDate.of(2025, 6, 2))
                .build();

        // when & then
        mockMvc.perform(put("/api/v1/task/schedule/{scheduleId}", 200L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("[일정 삭제] 성공")
    void testDeleteScheduleSuccess() throws Exception {
        // given
        setAuthentication(10L);

        // when & then
        mockMvc.perform(delete("/api/v1/task/schedule/{scheduleId}", 200L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("[일정 완료 상태 수정] 성공")
    void testUpdateCompletionStatusSuccess() throws Exception {
        // given
        setAuthentication(10L);

        CompletionStatusUpdateRequest request = new CompletionStatusUpdateRequest(true);

        ScheduleCommandResponse response = ScheduleCommandResponse.builder()
                .scheduleId(200L)
                .completionStatus(true)
                .build();

        when(scheduleCommandService.updateCompletionStatus(eq(200L), eq(true), eq(10L)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/task/schedule/{scheduleId}/completionstatus", 200L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scheduleId").value(200L))
                .andExpect(jsonPath("$.data.completionStatus").value(true));

        SecurityContextHolder.clearContext();
    }
}
