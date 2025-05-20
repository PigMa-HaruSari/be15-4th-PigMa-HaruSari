package com.pigma.harusari.category.command.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CategoryCreateRequest {
    /* 회원이 최초 회원가입할 때 사용하는 dto */
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String categoryName;

    private String color; // 선택 사항 (nullable)
}
