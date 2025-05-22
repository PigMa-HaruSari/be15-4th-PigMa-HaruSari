package com.pigma.harusari.user.command.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignOutRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}