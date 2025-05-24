package com.pigma.harusari.category.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateRequest {

    @NotBlank(message = "카테고리명은 필수입니다.")
    @Size(min = 2, max = 10, message = "카테고리명은 2자 이상 10자 이하로 입력해주세요.")
    private String categoryName;

    @NotBlank(message = "색상은 필수입니다.")
    private String color;
}
