package com.pigma.harusari.diary.command.application.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.diary.command.application.dto.request.DiaryCreateRequest;
import com.pigma.harusari.diary.command.application.dto.request.DiaryUpdateRequest;
import com.pigma.harusari.diary.command.application.service.DiaryCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diary")
@RequiredArgsConstructor
@Tag(name = "회고 명령 API", description = "회고 생성, 수정, 삭제 API")
public class DiaryCommandController {

    private final DiaryCommandService diaryCommandService;

    @PostMapping
    @Operation(summary = "회고 생성", description = "사용자 인증 후 회고를 생성한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회고 생성 성공")
    public ResponseEntity<ApiResponse<Long>> createDiary(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestBody @Valid DiaryCreateRequest request
    ) {
        Long diaryId = diaryCommandService.createDiary(userDetail.getMemberId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(diaryId));
    }

    @PutMapping("/{diaryId}")
    @Operation(summary = "회고 수정", description = "회고 ID와 사용자 인증 정보를 기반으로 회고를 수정한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회고 수정 성공")
    public ResponseEntity<ApiResponse<Void>> updateDiary(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long diaryId,
            @RequestBody @Valid DiaryUpdateRequest request
    ) {
        diaryCommandService.updateDiary(diaryId, userDetail.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "회고 삭제", description = "회고 ID와 사용자 인증 정보를 기반으로 회고를 삭제한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회고 삭제 성공")
    public ResponseEntity<ApiResponse<Void>> deleteDiary(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long diaryId
    ) {
        diaryCommandService.deleteDiary(diaryId, userDetail.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
