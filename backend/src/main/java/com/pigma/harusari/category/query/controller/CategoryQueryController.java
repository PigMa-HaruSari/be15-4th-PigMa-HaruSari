package com.pigma.harusari.category.query.controller;

import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import com.pigma.harusari.category.query.service.CategoryQueryService;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;  // 삭제
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "카테고리 조회 API", description = "사용자의 카테고리 조회 관련 API")
public class CategoryQueryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping("/categories")
    @Operation(
            summary = "사용자 카테고리 조회",
            description = "현재 로그인한 사용자의 카테고리 목록을 조회한다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getMyCategories(
            @AuthenticationPrincipal CustomUserDetails userDetail
    ) {
        List<CategoryResponse> categories = categoryQueryService.findCategoriesByMember(userDetail.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
}
