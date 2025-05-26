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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "회원 명령 API", description = "회원가입, 회원정보 수정, 비밀번호 변경 등")
public class UserCommandController {

    private final UserCommandService userCommandService;

    @PostMapping("/auth/signup")
    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공")
            }
    )
    public ResponseEntity<ApiResponse<Void>> signup(
            @Valid @RequestBody SignUpRequest request
    ) {
        userCommandService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PutMapping("/users/mypage")
    @Operation(
            summary = "회원 프로필 수정",
            description = "마이페이지에서 사용자 이름, 닉네임 등 프로필 정보를 수정합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필 수정 성공")
            }
    )
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        userCommandService.updateUserProfile(userDetails.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/users/password")
    @Operation(
            summary = "비밀번호 변경",
            description = "로그인 상태에서 비밀번호를 변경합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 변경 성공")
            }
    )
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userCommandService.changePassword(userDetails.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/users/signout")
    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴 요청을 처리합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
            }
    )
    public ResponseEntity<ApiResponse<Void>> signOut(
            @Valid @RequestBody SignOutRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userCommandService.signOut(userDetails.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/users/reset-password")
    @Operation(
            summary = "비밀번호 초기화",
            description = "비밀번호 찾기를 통해 임시 비밀번호를 발급합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 초기화 성공")
            }
    )
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestBody @Validated ResetPasswordPerformRequest request
    ) {
        userCommandService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
