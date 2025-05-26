package com.pigma.harusari.common.oauth.controller;

import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.common.oauth.dto.KakaoSignupRequest;
import com.pigma.harusari.common.oauth.dto.KakaoUserBasicInfo;
import com.pigma.harusari.common.oauth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth/social")
@RequiredArgsConstructor
@Tag(name = "소셜 로그인", description = "카카오 로그인 및 회원가입 API")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/info/kakao")
    @Operation(
            summary = "카카오 회원가입 - 사용자 정보 조회",
            description = "인가 코드로 카카오 사용자 정보를 조회합니다. (실제 회원가입은 아님)",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "사용자 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = KakaoUserBasicInfo.class))
                    )
            }
    )
    public ResponseEntity<ApiResponse<KakaoUserBasicInfo>> getKakaoUserInfo(
            @RequestParam String code
    ) {
        KakaoUserBasicInfo userInfo = kakaoAuthService.getUserInfo(code);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @PostMapping("/signup/")
    @Operation(
            summary = "카카오 회원가입 - 최종 처리",
            description = "카카오 사용자 정보를 기반으로 실제 회원가입 처리합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))
                    )
            }
    )
    public ResponseEntity<ApiResponse<LoginResponse>> signupWithKakao(
            @RequestBody KakaoSignupRequest request
    ) {
        LoginResponse response = kakaoAuthService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/login/")
    @Operation(
            summary = "카카오 로그인",
            description = "인가 코드를 이용해 카카오 계정으로 로그인합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))
                    )
            }
    )
    public ResponseEntity<ApiResponse<LoginResponse>> loginWithKakao(
            @RequestParam String code
    ) {
        LoginResponse response = kakaoAuthService.login(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
