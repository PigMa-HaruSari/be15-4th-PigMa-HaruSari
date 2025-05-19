package com.pigma.harusari.category.command.domain.repository;

import com.pigma.harusari.category.command.domain.aggregate.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 카테고리 이름 중복 확인
    boolean existsByMemberIdAndCategoryName(Long memberId, String categoryName);

    // 본인 카테고리만 삭제 가능
    Optional<Category> findByIdAndMemberId(Long categoryId, Long memberId);

    List<Category> findByMemberId(Long memberId);

}
