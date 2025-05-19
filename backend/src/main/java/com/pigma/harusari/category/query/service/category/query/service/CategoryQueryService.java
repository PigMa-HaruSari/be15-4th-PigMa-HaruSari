package com.pigma.harusari.category.query.service.category.query.service;

import com.pigma.harusari.category.query.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryQueryService {
    List<CategoryResponse> findCategoriesByMember(Long memberId);
}
