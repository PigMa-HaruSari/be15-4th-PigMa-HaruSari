package com.pigma.harusari.feedback.query.mapper;

import com.pigma.harusari.feedback.query.dto.FeedbackDetailDto;
import com.pigma.harusari.feedback.query.dto.FeedbackSummaryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedbackQueryMapper {
    List<FeedbackSummaryDto> findAllByMemberId(@Param("memberId") Long memberId);
    FeedbackDetailDto findByIdAndMemberId(@Param("feedbackId") Long feedbackId, @Param("memberId") Long memberId);
}
