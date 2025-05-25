package com.pigma.harusari.user.query.controller;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.query.dto.UserProfileResponse;
import com.pigma.harusari.user.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "회원 조회 API", description = "회원 마이페이지 정보 조회")
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/mypage")
    @Operation(
            summary = "마이페이지 정보 조회",
            description = "현재 로그인한 사용자의 마이페이지 정보를 조회합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "회원 정보 조회 성공"
                    )
            }
    )
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getMemberId();
        UserProfileResponse profile = userQueryService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}
