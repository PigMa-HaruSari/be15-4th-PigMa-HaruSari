package com.pigma.harusari.category.command.application.controller;


import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;
import com.pigma.harusari.category.command.application.dto.response.CategoryCommandResponse;
import com.pigma.harusari.category.command.domain.service.CategoryCommandService;
import com.pigma.harusari.category.command.domain.service.CategoryCommandServiceImpl;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryCommandController {

    private final CategoryCommandService categoryCommandService;


    // 카테고리 생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryCommandResponse>> createCategory(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        System.out.println("memberId: " + userDetail.getMemberId());

        Long categoryId = categoryCommandService.createCategory(userDetail.getMemberId(), request);
        CategoryCommandResponse response = new CategoryCommandResponse(categoryId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateCategory(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequest request
    ) {
        categoryCommandService.updateCategory(categoryId, userDetail.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 카테고리 완료 처리
    @PutMapping("/{categoryId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeCategory(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long categoryId
    ) {
        categoryCommandService.completeCategory(categoryId, userDetail.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long categoryId,
            @RequestParam String confirmText,
            @RequestParam(defaultValue = "false") boolean hasSchedules
    ) {
        categoryCommandService.deleteCategory(categoryId, userDetail.getMemberId(), confirmText, hasSchedules);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
