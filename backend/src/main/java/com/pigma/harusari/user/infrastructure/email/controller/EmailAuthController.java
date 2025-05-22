package com.pigma.harusari.user.infrastructure.email.controller;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.infrastructure.email.dto.EmailRequest;
import com.pigma.harusari.user.infrastructure.email.service.EmailAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/email")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    // 인증코드 발송
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode(@RequestBody @Valid EmailRequest request) {
        emailAuthService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 인증코드 검증
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyCode(@RequestBody @Valid EmailRequest request) {
        emailAuthService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}