package com.pigma.harusari.user.command.dto;

import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class SignUpRequest {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Size(min = 10, max = 20, message = "비밀번호는 10자 이상 20자 이하여야 합니다.")
    private String password;

    @Size(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하여야 합니다.")
    private String nickname;

    @NotBlank
    private String gender; // "NONE", "MALE", "FEMALE"

    @NotEmpty(message = "카테고리는 최소 1개 이상 입력해야 합니다.")
    private List<CategoryCreateRequest> categoryList;

}