package com.pigma.harusari.common.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoSignupRequest {

    private String email;
    private String nickname;
    private Boolean consentPersonalInfo;

}