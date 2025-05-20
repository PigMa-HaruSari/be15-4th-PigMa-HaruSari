package com.pigma.harusari.user.query.controller;

import com.pigma.harusari.common.dto.ApiResponse;
import com.pigma.harusari.user.query.dto.UserProfileResponse;
import com.pigma.harusari.user.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/mypage")
    public ApiResponse<UserProfileResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = Long.valueOf(userDetails.getUsername());
        UserProfileResponse profile = userQueryService.getUserProfile(userId);
        return ApiResponse.success(profile);
    }

}