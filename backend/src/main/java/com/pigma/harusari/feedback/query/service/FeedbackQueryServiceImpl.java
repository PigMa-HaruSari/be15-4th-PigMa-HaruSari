package com.pigma.harusari.feedback.query.service;

import com.pigma.harusari.feedback.query.dto.FeedbackDetailDto;
import com.pigma.harusari.feedback.query.dto.FeedbackSummaryDto;
import com.pigma.harusari.feedback.query.mapper.FeedbackQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackQueryServiceImpl implements FeedbackQueryService {

    private final FeedbackQueryMapper feedbackQueryMapper;

    @Override
    @Transactional
    public List<FeedbackSummaryDto> getFeedbackList(Long memberId) {
        return feedbackQueryMapper.findAllByMemberId(memberId);
    }

    @Override
    @Transactional
    public FeedbackDetailDto getFeedbackDetail(Long memberId, Long feedbackId) {
        return feedbackQueryMapper.findByIdAndMemberId(feedbackId, memberId);
    }
}
