package com.pigma.harusari.category.query.mapper;

import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryResponse> findByMemberId(@Param("memberId") Long memberId);
}
