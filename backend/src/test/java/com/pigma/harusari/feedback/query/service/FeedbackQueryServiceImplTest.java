package com.pigma.harusari.feedback.query.service;

import com.pigma.harusari.feedback.query.dto.FeedbackDetailDto;
import com.pigma.harusari.feedback.query.dto.FeedbackSummaryDto;
import com.pigma.harusari.feedback.query.mapper.FeedbackQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[피드백 - service] FeedbackQueryServiceImplTest 테스트")
class FeedbackQueryServiceImplTest {

    @Mock
    private FeedbackQueryMapper feedbackQueryMapper;

    @InjectMocks
    private FeedbackQueryServiceImpl feedbackQueryService;

    private final Long MEMBER_ID = 1L;

    private List<FeedbackSummaryDto> feedbackSummaryList;
    private FeedbackDetailDto feedbackDetailDto;

    @BeforeEach
    void setUp() {
        feedbackSummaryList = List.of(
                new FeedbackSummaryDto(1L, LocalDate.of(2025, 5, 1)),
                new FeedbackSummaryDto(2L, LocalDate.of(2025, 4, 1))
        );

        feedbackDetailDto = new FeedbackDetailDto(1L, "좋은 피드백 내용입니다.", LocalDate.of(2025, 5, 20));
    }

    @Test
    @DisplayName("피드백 목록 조회 - 성공")
    void getFeedbackList_success() {
        when(feedbackQueryMapper.findAllByMemberId(MEMBER_ID)).thenReturn(feedbackSummaryList);

        List<FeedbackSummaryDto> result = feedbackQueryService.getFeedbackList(MEMBER_ID);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getFeedbackId()).isEqualTo(1L);
        assertThat(result.get(1).getDate()).isEqualTo(LocalDate.of(2025, 4, 1));
    }

    @Test
    @DisplayName("피드백 상세 조회 - 성공")
    void getFeedbackDetail_success() {
        Long feedbackId = 1L;
        when(feedbackQueryMapper.findByIdAndMemberId(feedbackId, MEMBER_ID)).thenReturn(feedbackDetailDto);

        FeedbackDetailDto result = feedbackQueryService.getFeedbackDetail(MEMBER_ID, feedbackId);

        assertThat(result).isNotNull();
        assertThat(result.getFeedbackId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("좋은 피드백 내용입니다.");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2025, 5, 20));
    }
}
