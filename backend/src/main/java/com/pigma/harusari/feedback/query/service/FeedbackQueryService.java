package com.pigma.harusari.feedback.query.service;

import com.pigma.harusari.feedback.query.dto.FeedbackDetailDto;
import com.pigma.harusari.feedback.query.dto.FeedbackSummaryDto;

import java.util.List;

public interface FeedbackQueryService {
    List<FeedbackSummaryDto> getFeedbackList(Long memberId);
    FeedbackDetailDto getFeedbackDetail(Long memberId, Long feedbackId);
}
