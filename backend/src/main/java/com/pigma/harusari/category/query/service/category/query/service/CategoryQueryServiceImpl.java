package com.pigma.harusari.category.query.service.category.query.service;

import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import com.pigma.harusari.category.query.mapper.CategoryMapper;
import com.pigma.harusari.category.query.service.category.query.service.CategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findCategoriesByMember(Long memberId) {
        return categoryMapper.findByMemberId(memberId);
    }
}
