package com.pigma.harusari.common.oauth.dto;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class KakaoSignupRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "성별은 필수입니다.")
    private String gender; // "MALE", "FEMALE", "NONE"

    @NotNull(message = "개인정보 수집 동의는 필수입니다.")
    private Boolean consentPersonalInfo;

    @NotEmpty(message = "최소 하나 이상의 카테고리를 선택해야 합니다.")
    private List<CategoryCreateRequest> categoryList;
}