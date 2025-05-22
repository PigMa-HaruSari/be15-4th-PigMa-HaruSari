package com.pigma.harusari.diary.command.application.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.diary.command.application.dto.request.DiaryCreateRequest;
import com.pigma.harusari.diary.command.application.dto.request.DiaryUpdateRequest;
import com.pigma.harusari.diary.command.application.service.DiaryCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diary")
@RequiredArgsConstructor
public class DiaryCommandController {

    private final DiaryCommandService diaryCommandService;

    // ✅ 회고 작성
    @PostMapping("create")
    public ResponseEntity<ApiResponse<Long>> createDiary(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestBody @Valid DiaryCreateRequest request
    ) {
        Long diaryId = diaryCommandService.createDiary(userDetail.getMemberId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(diaryId));
    }

    // ✅ 회고 수정
    @PutMapping("/{diaryId}")
    public ResponseEntity<ApiResponse<Void>> updateDiary(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long diaryId,
            @RequestBody @Valid DiaryUpdateRequest request
    ) {
        diaryCommandService.updateDiary(diaryId, userDetail.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ✅ 회고 삭제
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<ApiResponse<Void>> deleteDiary(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long diaryId
    ) {
        diaryCommandService.deleteDiary(diaryId, userDetail.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
