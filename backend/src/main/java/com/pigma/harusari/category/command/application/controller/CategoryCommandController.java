package com.pigma.harusari.category.command.application.controller;


import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;
import com.pigma.harusari.category.command.application.dto.response.CategoryCommandResponse;
import com.pigma.harusari.category.command.domain.service.CategoryCommandServiceImpl;
import com.pigma.harusari.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")

@RequiredArgsConstructor
public class CategoryCommandController {

    private final CategoryCommandServiceImpl categoryCommandServiceImpl;


    // 카테고리 생성
    @PostMapping("")
    public ResponseEntity<ApiResponse<CategoryCommandResponse>> createCategory(
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        Long categoryId = categoryCommandServiceImpl.createCategory(request);

        CategoryCommandResponse response = new CategoryCommandResponse(categoryId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateCategory(
            @PathVariable Long categoryId,
            @RequestParam Long memberId,
            @RequestBody @Valid CategoryUpdateRequest request
    ) {
        categoryCommandServiceImpl.updateCategory(categoryId, memberId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 카테고리 완료 처리
    @PatchMapping("/{categoryId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeCategory(
            @PathVariable Long categoryId,
            @RequestParam Long memberId
    ) {
        categoryCommandServiceImpl.completeCategory(categoryId, memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long categoryId,
            @RequestParam Long memberId,
            @RequestParam String confirmText,
            @RequestParam(defaultValue = "false") boolean hasSchedules
    ) {
        categoryCommandServiceImpl.deleteCategory(categoryId, memberId, confirmText, hasSchedules);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
