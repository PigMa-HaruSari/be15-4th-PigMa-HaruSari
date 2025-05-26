package com.pigma.harusari.user.infrastructure.email.controller;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.infrastructure.email.dto.EmailRequest;
import com.pigma.harusari.user.infrastructure.email.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/email")
@RequiredArgsConstructor
@Tag(name = "이메일 인증 API", description = "이메일 기반 인증코드 발송 및 검증")
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @PostMapping("/send")
    @Operation(
            summary = "인증코드 발송",
            description = "입력한 이메일 주소로 인증코드를 발송합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증코드 발송 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 이메일 형식 등")
            }
    )
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode(
            @RequestBody @Valid EmailRequest request
    ) {
        emailAuthService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/verify")
    @Operation(
            summary = "인증코드 검증",
            description = "입력한 이메일과 인증코드가 일치하는지 검증합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "인증 실패 (코드 불일치 또는 만료 등)")
            }
    )
    public ResponseEntity<ApiResponse<Void>> verifyCode(
            @RequestBody @Valid EmailRequest request
    ) {
        emailAuthService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
