package com.pigma.harusari.user.infrastructure.email.controller;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.infrastructure.email.dto.PasswordResetRequest;
import com.pigma.harusari.user.infrastructure.email.dto.ResetTokenVerifyRequest;
import com.pigma.harusari.user.infrastructure.email.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/reset-password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Void>> sendResetPasswordLink(
            @RequestBody @Valid PasswordResetRequest request
    ) {
        passwordResetService.sendResetLink(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyResetToken(@RequestBody @Valid ResetTokenVerifyRequest request) {
        passwordResetService.verifyToken(request.getEmail(), request.getToken());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}