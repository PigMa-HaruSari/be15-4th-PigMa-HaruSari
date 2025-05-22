package com.pigma.harusari.user.command.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.command.dto.*;
import com.pigma.harusari.user.command.service.UserCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserCommandController {

    private final UserCommandService userCommandService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @Valid @RequestBody SignUpRequest request
    ) {
        userCommandService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PutMapping("/users/mypage")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        userCommandService.updateUserProfile(userDetails.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/users/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userCommandService.changePassword(userDetails.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/users/signout")
    public ResponseEntity<ApiResponse<Void>> signOut(
            @Valid @RequestBody SignOutRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userCommandService.signOut(userDetails.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/users/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestBody @Validated ResetPasswordPerformRequest request
    ) {
        userCommandService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}