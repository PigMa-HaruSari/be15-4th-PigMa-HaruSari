package com.pigma.harusari.category.query.controller;

import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import com.pigma.harusari.category.query.service.category.query.service.CategoryQueryService;
import com.pigma.harusari.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryQueryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getMyCategories(
            @RequestParam Long memberId
    ) {
        List<CategoryResponse> categories = categoryQueryService.findCategoriesByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
}
