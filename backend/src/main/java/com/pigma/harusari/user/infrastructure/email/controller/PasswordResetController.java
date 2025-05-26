package com.pigma.harusari.user.infrastructure.email.controller;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.infrastructure.email.dto.PasswordResetRequest;
import com.pigma.harusari.user.infrastructure.email.dto.ResetTokenVerifyRequest;
import com.pigma.harusari.user.infrastructure.email.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/reset-password")
@RequiredArgsConstructor
@Tag(name = "비밀번호 재설정 API", description = "비밀번호 재설정 링크 요청 및 토큰 검증 API")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request")
    @Operation(
            summary = "비밀번호 재설정 링크 요청",
            description = "사용자의 이메일을 받아 비밀번호 재설정 링크를 전송한다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "비밀번호 재설정 링크 전송 성공"
    )
    public ResponseEntity<ApiResponse<Void>> sendResetPasswordLink(
            @RequestBody @Valid PasswordResetRequest request
    ) {
        passwordResetService.sendResetLink(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/verify")
    @Operation(
            summary = "비밀번호 재설정 토큰 검증",
            description = "사용자의 이메일과 토큰을 받아 유효성을 검증한다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "비밀번호 재설정 토큰 검증 성공"
    )
    public ResponseEntity<ApiResponse<Void>> verifyResetToken(
            @RequestBody @Valid ResetTokenVerifyRequest request
    ) {
        passwordResetService.verifyToken(request.getEmail(), request.getToken());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
