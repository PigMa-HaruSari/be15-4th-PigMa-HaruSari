package com.pigma.harusari.alarm.query.controller;

import com.pigma.harusari.alarm.query.dto.AlarmResponseDto;
import com.pigma.harusari.alarm.query.service.AlarmQueryService;
import com.pigma.harusari.support.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlarmQueryController.class)
@DisplayName("[알림 조회 - controller] AlarmQueryController 테스트")
public class AlarmQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlarmQueryService alarmQueryService;

    @Test
    @WithMockCustomUser(memberId = 1L)
    @DisplayName("[미확인 알림 조회] 인증된 사용자로 요청 시 성공")
    void testGetUnreadAlarms() throws Exception {
        // given
        AlarmResponseDto alarm = AlarmResponseDto.builder()
                .alarmId(1L)
                .alarmMessage("알림 테스트 메시지")
                .type("DAILY")
                .isRead(false)
                .createdAt(new Date())
                .build();

        when(alarmQueryService.getUnreadAlarms(1L)).thenReturn(List.of(alarm));

        // when & then
        mockMvc.perform(get("/api/v1/alarms/unread")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].alarmId").value(1))
                .andExpect(jsonPath("$.data[0].alarmMessage").value("알림 테스트 메시지"))
                .andExpect(jsonPath("$.data[0].type").value("DAILY"))
                .andExpect(jsonPath("$.data[0].isRead").value(false))
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist());
    }
}
