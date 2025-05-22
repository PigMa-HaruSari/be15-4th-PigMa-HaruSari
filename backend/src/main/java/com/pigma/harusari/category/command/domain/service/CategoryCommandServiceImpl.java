package com.pigma.harusari.category.command.domain.service;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;
import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryCommandRepository categoryCommandRepository;

    @Override
    public Long createCategory(Long memberId, CategoryCreateRequest request) {
        System.out.println("[카테고리 생성 요청] memberId=" + memberId + ", name=" + request.getCategoryName());

        boolean exists = categoryCommandRepository.existsByMemberIdAndCategoryName(memberId, request.getCategoryName());
        System.out.println("[중복 검사 결과] exists=" + exists);

//        if (categoryCommandRepository.existsByMemberIdAndCategoryName(memberId, request.getCategoryName())) {
//            throw new IllegalStateException("이미 동일한 이름의 카테고리가 존재합니다.");
//        }


        Category category = Category.builder()
                .memberId(memberId)
                .categoryName(request.getCategoryName())
                .color(request.getColor())
                .completed(false)
                .build();

        return categoryCommandRepository.save(category).getCategoryId();
    }

    @Override
    public void updateCategory(Long categoryId, Long memberId, CategoryUpdateRequest request) {
        Category category = categoryCommandRepository.findByCategoryIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 같은 이름이지만 본인 카테고리가 아닌 경우만 체크
        if (!category.getCategoryName().equals(request.getCategoryName()) &&
                categoryCommandRepository.existsByMemberIdAndCategoryName(memberId, request.getCategoryName())) {
            throw new IllegalStateException("이미 동일한 이름의 카테고리가 존재합니다.");
        }

        category.updateCategoryDetails(request.getCategoryName(), request.getColor());
    }

    @Override
    public void completeCategory(Long categoryId, Long memberId) {
        Category category = categoryCommandRepository.findByCategoryIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        category.completeCategory();
    }

    @Override
    public void deleteCategory(Long categoryId, Long memberId, String confirmText, boolean hasSchedules) {
        if (!"카테고리를 삭제하겠습니다.".equals(confirmText)) {
            throw new IllegalArgumentException("삭제 확인 문구가 일치하지 않습니다.");
        }

        Category category = categoryCommandRepository.findByCategoryIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 연결된 일정 삭제는 추후 처리
        categoryCommandRepository.delete(category);
    }
}
