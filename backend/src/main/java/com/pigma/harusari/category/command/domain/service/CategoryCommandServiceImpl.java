package com.pigma.harusari.category.command.domain.service;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;
import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Long createCategory(CategoryCreateRequest request) {
        Category category = Category.builder()
                .memberId(request.getMemberId())
                .categoryName(request.getCategoryName())
                .color(request.getColor())
                .completed(false)
                .build();

        return categoryRepository.save(category).getCategoryId();
    }

    @Override
    @Transactional
    public void updateCategory(Long categoryId, Long memberId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findByCategoryIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        category.updateCategoryDetails(request.getCategoryName(), request.getColor());
    }

    @Override
    @Transactional
    public void completeCategory(Long categoryId, Long memberId) {
        Category category = categoryRepository.findByCategoryIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        category.completeCategory();
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId, Long memberId, String confirmText, boolean hasSchedules) {
        if (!"카테고리를 삭제하겠습니다.".equals(confirmText)) {
            throw new IllegalArgumentException("삭제 확인 문구가 일치하지 않습니다.");
        }

        Category category = categoryRepository.findByCategoryIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 연결된 일정 삭제는 추후 처리
        categoryRepository.delete(category);
    }
}
