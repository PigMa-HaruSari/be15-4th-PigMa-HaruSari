package com.pigma.harusari.common.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {

    @Email
    @NotBlank
    private String email;

    private String code; // 인증 코드

}