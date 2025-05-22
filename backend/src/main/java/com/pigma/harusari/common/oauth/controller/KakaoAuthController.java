package com.pigma.harusari.common.oauth.controller;

import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.common.oauth.dto.KakaoSignupRequest;
import com.pigma.harusari.common.oauth.dto.KakaoUserBasicInfo;
import com.pigma.harusari.common.oauth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/social")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    /* 카카오 회원가입 - 사용자 정보만 조회(실제 가입은 X) */
    @GetMapping("/info/kakao")
    public ResponseEntity<ApiResponse<KakaoUserBasicInfo>> getKakaoUserInfo(
            @RequestParam String code
    ) {
        KakaoUserBasicInfo userInfo = kakaoAuthService.getUserInfo(code);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    /* 카카오 회원가입 - 최종 회원가입 처리(성별, 개인정보 동의 등) */
    @PostMapping("/signup/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> signupWithKakao(
            @RequestBody KakaoSignupRequest request
    ) {
        LoginResponse response = kakaoAuthService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 카카오 로그인 */
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> loginWithKakao(
            @RequestParam String code
    ) {
        LoginResponse response = kakaoAuthService.login(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
