package com.pigma.harusari.category.query.service;

import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import com.pigma.harusari.category.query.mapper.CategoryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@DisplayName("[카테고리 - queryService] CategoryQueryServiceImplTest 테스트")
class CategoryQueryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryQueryServiceImpl categoryQueryService;

    @Test
    @DisplayName("멤버 ID로 카테고리 목록을 조회한다")
    void testFindCategoriesByMember() {
        // given
        Long memberId = 1L;
        List<CategoryResponse> expected = List.of(
                new CategoryResponse(100L, "공부", "#123456", false),
                new CategoryResponse(101L, "운동", "#654321", true)
        );

        when(categoryMapper.findByMemberId(memberId)).thenReturn(expected);

        // when
        List<CategoryResponse> result = categoryQueryService.findCategoriesByMember(memberId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryId()).isEqualTo(100L);
        assertThat(result.get(1).getCategoryName()).isEqualTo("운동");
        assertThat(result.get(1).isCompleted()).isTrue();
    }
}
