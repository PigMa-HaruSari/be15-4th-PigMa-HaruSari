package com.pigma.harusari.category.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "member_uid")
    private Long memberId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "color")
    private String color;

    @Column(name = "completion_status")
    private boolean completed;

    @Builder
    public Category(Long categoryId, Long memberId, String categoryName, String color, boolean completed) {
        this.categoryId = categoryId;
        this.memberId = memberId;
        this.categoryName = categoryName;
        this.color = color;
        this.completed = completed;
    }


    public void updateCategoryDetails(String categoryName, String color) {
        this.categoryName = categoryName;
        this.color = color;
    }

    public void completeCategory() {
        this.completed = true;
    }
}

