package com.pigma.harusari.alarm.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.alarm.command.service.AlarmCommandServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlarmCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[알림 - controller] AlarmCommandController 테스트")
class AlarmCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlarmCommandServiceImpl alarmCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "1") // username이 Long형 memberId로 사용됨
    @DisplayName("[알림 전체 읽음 처리] 인증된 사용자로 요청 시 성공")
    void testMarkAllAsReadSuccess() throws Exception {
        // given
        Long mockMemberId = 1L;

        doNothing().when(alarmCommandService).markAllAsRead(mockMemberId);

        // when & then
        mockMvc.perform(put("/api/v1/alarms/read-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist()) // null로 반환되는 경우
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist());
    }
}
