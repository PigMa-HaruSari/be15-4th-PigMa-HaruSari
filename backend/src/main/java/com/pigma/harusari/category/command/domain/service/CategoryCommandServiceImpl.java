package com.pigma.harusari.category.command.domain.service;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;
import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import com.pigma.harusari.category.exception.CategoryErrorCode;
import com.pigma.harusari.category.exception.CategoryException;
import com.pigma.harusari.task.schedule.command.repository.ScheduleRepository;
import com.pigma.harusari.task.schedule.command.service.ScheduleCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryCommandRepository categoryCommandRepository;
    private final ScheduleCommandService scheduleCommandService;


    @Override
    @Transactional
    public Long createCategory(Long memberId, CategoryCreateRequest request) {
        System.out.println("[카테고리 생성 요청] memberId=" + memberId + ", name=" + request.getCategoryName());

        boolean exists = categoryCommandRepository.existsByMemberIdAndCategoryName(memberId, request.getCategoryName());
        System.out.println("[중복 검사 결과] exists=" + exists);

        if (exists) {
            throw new CategoryException(CategoryErrorCode.DUPLICATE_CATEGORY);
        }

        Category category = Category.builder()
                .memberId(memberId)
                .categoryName(request.getCategoryName())
                .color(request.getColor())
                .completed(false)
                .build();

        Category saved = categoryCommandRepository.save(category);

        if (saved == null || saved.getCategoryId() == null) {
            throw new CategoryException(CategoryErrorCode.FAILED_TO_SAVE_CATEGORY);
        }

        return saved.getCategoryId();
    }

    @Override
    @Transactional
    public void updateCategory(Long categoryId, Long memberId, CategoryUpdateRequest request) {
        Category category = categoryCommandRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getMemberId().equals(memberId)) {
            throw new CategoryException(CategoryErrorCode.CATEGORY_ACCESS_DENIED);
        }

        category.updateCategoryDetails(request.getCategoryName(), request.getColor());
    }

    @Override
    @Transactional
    public void completeCategory(Long categoryId, Long memberId) {
        Category category = categoryCommandRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getMemberId().equals(memberId)) {
            throw new CategoryException(CategoryErrorCode.CATEGORY_ACCESS_DENIED);
        }

        category.completeCategory();
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId, Long memberId, String confirmText, boolean hasSchedules) {
        if (!"카테고리를 삭제하겠습니다".equals(confirmText.trim())) {
            // 삭제 메시지 확인 에러코드
            throw new CategoryException(CategoryErrorCode.CONFIRMATION_TEXT_MISMATCH);
        }

        // 카테고리 존재 확인
        Category category = categoryCommandRepository.findByCategoryIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        if (hasSchedules) {
            // 일정이 포함 확인
            throw new CategoryException(CategoryErrorCode.CANNOT_DELETE_CATEGORY_WITH_SCHEDULES);
        }
        scheduleCommandService.deleteSchedulesByCategoryId(categoryId);


        categoryCommandRepository.delete(category);
    }
}
