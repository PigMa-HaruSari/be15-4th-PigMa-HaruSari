package com.pigma.harusari.user.command.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.command.dto.SignOutRequest;
import com.pigma.harusari.user.command.dto.SignUpRequest;
import com.pigma.harusari.user.command.dto.UpdatePasswordRequest;
import com.pigma.harusari.user.command.dto.UpdateUserProfileRequest;
import com.pigma.harusari.user.command.service.UserCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PutMapping("/user/mypage")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        userCommandService.updateUserProfile(userDetails.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/user/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        userCommandService.changePassword(user.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/user/signout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> signOut(
            @Valid @RequestBody SignOutRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        userCommandService.signOut(user.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}