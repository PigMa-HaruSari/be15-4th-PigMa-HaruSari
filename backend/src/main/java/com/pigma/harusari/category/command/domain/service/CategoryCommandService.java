package com.pigma.harusari.category.command.domain.service;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;

public interface CategoryCommandService {

    Long createCategory(CategoryCreateRequest request);

    void updateCategory(Long categoryId, Long memberId, CategoryUpdateRequest request);

    void completeCategory(Long categoryId, Long memberId);

    void deleteCategory(Long categoryId, Long memberId, String confirmText, boolean hasSchedules);
}