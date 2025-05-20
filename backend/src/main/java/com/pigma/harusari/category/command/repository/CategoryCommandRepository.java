package com.pigma.harusari.category.command.repository;

import com.pigma.harusari.category.command.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryCommandRepository extends JpaRepository<Category, Long> {
}
