package com.pigma.harusari.category.command.application.controller;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;
import com.pigma.harusari.category.command.application.dto.response.CategoryCommandResponse;
import com.pigma.harusari.category.command.domain.service.CategoryCommandService;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "카테고리 명령 API", description = "카테고리 생성, 수정, 완료 처리, 삭제 API")
public class CategoryCommandController {

    private final CategoryCommandService categoryCommandService;

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "사용자 인증 후 카테고리를 생성한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "카테고리 생성 성공")
    public ResponseEntity<ApiResponse<CategoryCommandResponse>> createCategory(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        Long categoryId = categoryCommandService.createCategory(userDetail.getMemberId(), request);
        CategoryCommandResponse response = new CategoryCommandResponse(categoryId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "카테고리 ID와 사용자 인증을 통해 카테고리를 수정한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
    public ResponseEntity<ApiResponse<Void>> updateCategory(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequest request
    ) {
        categoryCommandService.updateCategory(categoryId, userDetail.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{categoryId}/complete")
    @Operation(summary = "카테고리 완료 처리", description = "카테고리를 완료 상태로 변경한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 완료 처리 성공")
    public ResponseEntity<ApiResponse<Void>> completeCategory(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long categoryId
    ) {
        categoryCommandService.completeCategory(categoryId, userDetail.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제 요청. confirmText와 hasSchedules 쿼리 파라미터로 삭제 조건을 명시한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 삭제 성공")
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
