package com.pigma.harusari.category.query.service.category.query.service;

import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryRepository;
import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> findCategoriesByMember(Long memberId) {
        List<Category> categories = categoryRepository.findByMemberId(memberId);

        return categories.stream()
                .map(category -> new CategoryResponse(
                        category.getCategoryId(),
                        category.getCategoryName(),
                        category.getColor(),
                        category.isCompleted()
                )).collect(Collectors.toList());
    }
}