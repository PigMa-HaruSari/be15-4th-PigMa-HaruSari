package com.pigma.harusari.feedback.query.service;

import com.pigma.harusari.feedback.query.dto.FeedbackDetailDto;
import com.pigma.harusari.feedback.query.dto.FeedbackSummaryDto;
import com.pigma.harusari.feedback.query.mapper.FeedbackQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackQueryService {

    private final FeedbackQueryMapper feedbackQueryMapper;

    public List<FeedbackSummaryDto> getFeedbackList(Long memberId) {
        return feedbackQueryMapper.findAllByMemberId(memberId);
    }

    public FeedbackDetailDto getFeedbackDetail(Long memberId, Long feedbackId) {
        return feedbackQueryMapper.findByIdAndMemberId(feedbackId, memberId);
    }
}
