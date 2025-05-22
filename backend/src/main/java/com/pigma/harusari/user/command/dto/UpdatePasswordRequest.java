package com.pigma.harusari.user.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePasswordRequest {

    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    private String currentPassword;

    @Size(min = 10, message = "새 비밀번호는 10자 이상이어야 합니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String confirmPassword;
}