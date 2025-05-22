package com.pigma.harusari.feedback.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.feedback.command.service.FeedbackCommandService;
import com.pigma.harusari.feedback.query.dto.FeedbackDetailDto;
import com.pigma.harusari.feedback.query.dto.FeedbackSummaryDto;
import com.pigma.harusari.feedback.query.service.FeedbackQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class FeedbackQueryController {

    private final FeedbackQueryService feedbackQueryService;

    @GetMapping
    public ApiResponse<List<FeedbackSummaryDto>> getFeedbackList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        List<FeedbackSummaryDto> feedbackList = feedbackQueryService.getFeedbackList(memberId);
        return ApiResponse.success(feedbackList);
    }

    @GetMapping("/{feedbackId}")
    public ApiResponse<FeedbackDetailDto> getFeedbackDetail(@PathVariable Long feedbackId,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        FeedbackDetailDto feedbackDetail = feedbackQueryService.getFeedbackDetail(memberId, feedbackId);
        return ApiResponse.success(feedbackDetail);
    }


    // 피드백 강제 생성 테스트
    private final FeedbackCommandService feedbackCommandService;
    // 특정 회원의 피드백을 강제로 생성하는 테스트용 API
    @PostMapping("/generate/{memberId}")
    public String generateFeedbackForMember(@PathVariable Long memberId) {
        feedbackCommandService.generateMonthlyFeedbackForMember(memberId);
        return "Feedback generation request completed for memberId = " + memberId;
    }
}
