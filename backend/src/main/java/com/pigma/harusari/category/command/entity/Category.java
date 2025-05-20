package com.pigma.harusari.category.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "completion_status", nullable = false)
    private boolean completionStatus;

    @Builder
    public Category(Long memberId, String categoryName, String color, boolean completionStatus) {
        this.memberId = memberId;
        this.categoryName = categoryName;
        this.color = color;
        this.completionStatus = completionStatus;
    }
}