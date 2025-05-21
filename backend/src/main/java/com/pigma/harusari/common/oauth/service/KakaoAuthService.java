package com.pigma.harusari.common.oauth.service;

import com.pigma.harusari.common.oauth.dto.KakaoUserBasicInfo;

public interface KakaoAuthService {

    KakaoUserBasicInfo getUserInfo(String code);

}
