package com.pigma.harusari.category.command.domain.service;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;
import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import com.pigma.harusari.category.exception.CategoryErrorCode;
import com.pigma.harusari.category.exception.CategoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[카테고리 - service] CategoryCommandServiceImpl 테스트")
class CategoryCommandServiceImplTest {

    @InjectMocks
    CategoryCommandServiceImpl categoryCommandService;

    @Mock
    CategoryCommandRepository categoryCommandRepository;

    private final Long memberId = 1L;
    private final Long categoryId = 100L;

    Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .categoryId(categoryId)
                .memberId(memberId)
                .categoryName("공부")
                .color("#123456")
                .completed(false)
                .build();
    }

    @Test
    @DisplayName("카테고리를 성공적으로 생성하는 테스트")
    void testCreateCategorySuccess() {
        CategoryCreateRequest request = new CategoryCreateRequest(memberId, "공부", "#123456");

        when(categoryCommandRepository.existsByMemberIdAndCategoryName(memberId, "공부")).thenReturn(false);
        when(categoryCommandRepository.save(any(Category.class))).thenReturn(category);

        Long savedCategoryId = categoryCommandService.createCategory(memberId, request);

        assertThat(savedCategoryId).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("중복된 이름의 카테고리를 생성하려 할 때 예외 발생 테스트")
    void testCreateCategoryDuplicateName() {
        CategoryCreateRequest request = new CategoryCreateRequest(memberId, "공부", "#123456");

        when(categoryCommandRepository.existsByMemberIdAndCategoryName(memberId, "공부")).thenReturn(true);

        assertThatThrownBy(() -> categoryCommandService.createCategory(memberId, request))
                .isInstanceOf(CategoryException.class)
                .hasMessageContaining(CategoryErrorCode.DUPLICATE_CATEGORY.getErrorMessage());
    }

    @Test
    @DisplayName("카테고리 수정 성공 테스트")
    void testUpdateCategorySuccess() {
        CategoryUpdateRequest request = new CategoryUpdateRequest("운동", "#654321");

        when(categoryCommandRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryCommandService.updateCategory(categoryId, memberId, request);

        assertThat(category.getCategoryName()).isEqualTo("운동");
        assertThat(category.getColor()).isEqualTo("#654321");
    }

    @Test
    @DisplayName("카테고리 수정 시 소유자 불일치로 인한 접근 거부 예외 테스트")
    void testUpdateCategoryAccessDenied() {
        CategoryUpdateRequest request = new CategoryUpdateRequest("운동", "#654321");

        when(categoryCommandRepository.findById(categoryId)).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> categoryCommandService.updateCategory(categoryId, 999L, request))
                .isInstanceOf(CategoryException.class)
                .hasMessageContaining(CategoryErrorCode.CATEGORY_ACCESS_DENIED.getErrorMessage());
    }

    @Test
    @DisplayName("카테고리 완료 처리 성공 테스트")
    void testCompleteCategorySuccess() {
        when(categoryCommandRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryCommandService.completeCategory(categoryId, memberId);

        assertThat(category.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("카테고리 삭제 성공 테스트")
    void testDeleteCategorySuccess() {
        when(categoryCommandRepository.findByCategoryIdAndMemberId(categoryId, memberId)).thenReturn(Optional.of(category));

        categoryCommandService.deleteCategory(categoryId, memberId, "카테고리를 삭제하겠습니다", false);

        verify(categoryCommandRepository).delete(category);
    }

    @Test
    @DisplayName("카테고리 삭제 시 확인 문구 불일치 예외 테스트")
    void testDeleteCategoryWithWrongConfirmText() {
        assertThatThrownBy(() -> categoryCommandService.deleteCategory(categoryId, memberId, "삭제하겠습니다.", false))
                .isInstanceOf(CategoryException.class)
                .hasMessageContaining(CategoryErrorCode.CONFIRMATION_TEXT_MISMATCH.getErrorMessage());
    }

    @Test
    @DisplayName("카테고리 삭제 시 일정 포함으로 인한 예외 발생 테스트")
    void testDeleteCategoryWithSchedules() {
        when(categoryCommandRepository.findByCategoryIdAndMemberId(categoryId, memberId)).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> categoryCommandService.deleteCategory(categoryId, memberId, "카테고리를 삭제하겠습니다", true))
                .isInstanceOf(CategoryException.class)
                .hasMessageContaining(CategoryErrorCode.CANNOT_DELETE_CATEGORY_WITH_SCHEDULES.getErrorMessage());
    }
}
