package com.pigma.harusari.feedback.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.feedback.command.service.FeedbackCommandService;
import com.pigma.harusari.feedback.query.dto.FeedbackDetailDto;
import com.pigma.harusari.feedback.query.dto.FeedbackSummaryDto;
import com.pigma.harusari.feedback.query.service.FeedbackQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@Tag(name = "피드백 조회 API", description = "피드백 목록 및 상세 조회, 테스트용 강제 생성 API")
public class FeedbackQueryController {

    private final FeedbackQueryService feedbackQueryService;
    private final FeedbackCommandService feedbackCommandService;

    @GetMapping
    @Operation(summary = "피드백 목록 조회", description = "로그인한 사용자의 피드백 요약 리스트를 조회한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "피드백 목록 조회 성공")
    public ResponseEntity<ApiResponse<List<FeedbackSummaryDto>>> getFeedbackList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        List<FeedbackSummaryDto> feedbackList = feedbackQueryService.getFeedbackList(memberId);
        return ResponseEntity.ok(ApiResponse.success(feedbackList));
    }

    @GetMapping("/{feedbackId}")
    @Operation(summary = "피드백 상세 조회", description = "특정 피드백 ID에 대한 상세 정보를 조회한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "피드백 상세 조회 성공")
    public ResponseEntity<ApiResponse<FeedbackDetailDto>> getFeedbackDetail(
            @PathVariable Long feedbackId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        FeedbackDetailDto feedbackDetail = feedbackQueryService.getFeedbackDetail(memberId, feedbackId);
        return ResponseEntity.ok(ApiResponse.success(feedbackDetail));
    }

    // 테스트용 API: 특정 회원의 피드백을 강제로 생성
    @PostMapping("/generate/{memberId}")
    @Operation(summary = "피드백 강제 생성 테스트", description = "특정 회원에 대해 피드백을 강제로 생성한다. 테스트용 API임.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "피드백 생성 요청 완료")
    public ResponseEntity<String> generateFeedbackForMember(@PathVariable Long memberId) {
        feedbackCommandService.generateMonthlyFeedbackForMember(memberId);
        return ResponseEntity.ok("Feedback generation request completed for memberId = " + memberId);
    }
}
