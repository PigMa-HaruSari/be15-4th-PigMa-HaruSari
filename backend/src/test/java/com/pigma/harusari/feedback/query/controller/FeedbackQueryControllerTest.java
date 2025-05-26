package com.pigma.harusari.feedback.query.controller;

import com.pigma.harusari.feedback.command.service.FeedbackCommandService;
import com.pigma.harusari.feedback.query.controller.FeedbackQueryController;
import com.pigma.harusari.feedback.query.dto.FeedbackDetailDto;
import com.pigma.harusari.feedback.query.dto.FeedbackSummaryDto;
import com.pigma.harusari.feedback.query.service.FeedbackQueryService;
import com.pigma.harusari.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeedbackQueryController.class)
@DisplayName("[피드백 - controller] FeedbackQueryController 테스트")
class FeedbackQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FeedbackQueryService feedbackQueryService;

    // 여기에 추가
    @MockBean
    FeedbackCommandService feedbackCommandService;

    private final Long MEMBER_ID = 1L;

    List<FeedbackSummaryDto> feedbackSummaryList;
    FeedbackDetailDto feedbackDetailDto;

    @BeforeEach
    void setUp() {
        feedbackSummaryList = List.of(
                new FeedbackSummaryDto(1L, LocalDate.of(2025, 5, 1)),
                new FeedbackSummaryDto(2L, LocalDate.of(2025, 4, 1))
        );

        feedbackDetailDto = new FeedbackDetailDto(1L, "좋은 피드백 내용입니다.", LocalDate.of(2025, 5, 20));
    }

    @Test
    @DisplayName("[피드백 목록 조회] 성공 테스트")
    @WithMockCustomUser
    void testGetFeedbackList() throws Exception {
        when(feedbackQueryService.getFeedbackList(MEMBER_ID)).thenReturn(feedbackSummaryList);

        mockMvc.perform(get("/api/v1/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].feedbackId").value(feedbackSummaryList.get(0).getFeedbackId()))
                .andExpect(jsonPath("$.data[0].summary").value(feedbackSummaryList.get(0).getSummary()))
                .andExpect(jsonPath("$.data[0].date").value(feedbackSummaryList.get(0).getDate().toString()))
                .andExpect(jsonPath("$.data[1].feedbackId").value(feedbackSummaryList.get(1).getFeedbackId()))
                .andExpect(jsonPath("$.data[1].summary").value(feedbackSummaryList.get(1).getSummary()))
                .andExpect(jsonPath("$.data[1].date").value(feedbackSummaryList.get(1).getDate().toString()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[피드백 상세 조회] 성공 테스트")
    @WithMockCustomUser
    void testGetFeedbackDetail() throws Exception {
        Long feedbackId = 1L;

        when(feedbackQueryService.getFeedbackDetail(MEMBER_ID, feedbackId)).thenReturn(feedbackDetailDto);

        mockMvc.perform(get("/api/v1/feedbacks/{feedbackId}", feedbackId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.feedbackId").value(feedbackDetailDto.getFeedbackId()))
                .andExpect(jsonPath("$.data.content").value(feedbackDetailDto.getContent()))
                .andExpect(jsonPath("$.data.date").value(feedbackDetailDto.getDate().toString()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }
}
