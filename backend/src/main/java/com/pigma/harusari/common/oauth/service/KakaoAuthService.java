package com.pigma.harusari.common.oauth.service;

import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.oauth.dto.KakaoSignupRequest;
import com.pigma.harusari.common.oauth.dto.KakaoUserBasicInfo;

public interface KakaoAuthService {

    KakaoUserBasicInfo getUserInfo(String code);
    LoginResponse signup(KakaoSignupRequest request);
    LoginResponse login(String code);

}
