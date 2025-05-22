package com.pigma.harusari.category.query.controller;

import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import com.pigma.harusari.category.query.service.CategoryQueryService;
import com.pigma.harusari.category.query.service.CategoryQueryServiceImpl;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryQueryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getMyCategories(
            @AuthenticationPrincipal CustomUserDetails userDetail
    ) {
        List<CategoryResponse> categories = categoryQueryService.findCategoriesByMember(userDetail.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

}
