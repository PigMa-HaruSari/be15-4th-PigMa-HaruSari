package com.pigma.harusari.common.oauth.controller;

import com.pigma.harusari.common.dto.ApiResponse;
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

}
