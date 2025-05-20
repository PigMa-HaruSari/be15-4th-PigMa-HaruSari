package com.pigma.harusari.category.query.service.category.query.service;

import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import com.pigma.harusari.category.query.service.CategoryQueryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceImplTest {

    @Mock
    private CategoryCommandRepository categoryCommandRepository;

    @InjectMocks
    private CategoryQueryServiceImpl categoryQueryServiceImpl;

    @Test
    void findCategoriesByMember_정상조회() {
        // given
        Long memberId = 1L;

        Category category1 = Category.builder()
                .categoryId(10L)
                .categoryName("운동")
                .color("#FF0000")
                .completed(false)
                .build();

        Category category2 = Category.builder()
                .categoryId(11L)
                .categoryName("공부")
                .color("#00FF00")
                .completed(true)
                .build();

        when(categoryCommandRepository.findByMemberId(memberId))
                .thenReturn(Arrays.asList(category1, category2));

        // when
        List<CategoryResponse> result = categoryQueryServiceImpl.findCategoriesByMember(memberId);

        // then
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getCategoryId()).isEqualTo(10L);
        assertThat(result.get(0).getCategoryName()).isEqualTo("운동");
        assertThat(result.get(0).getColor()).isEqualTo("#FF0000");
        assertThat(result.get(0).isCompleted()).isFalse();

        assertThat(result.get(1).getCategoryId()).isEqualTo(11L);
        assertThat(result.get(1).getCategoryName()).isEqualTo("공부");
        assertThat(result.get(1).getColor()).isEqualTo("#00FF00");
        assertThat(result.get(1).isCompleted()).isTrue();
    }
}