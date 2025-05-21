package com.pigma.harusari.common.oauth.dto;

import lombok.Getter;

@Getter
public class KakaoSignupRequest {

    private String email;
    private String nickname;
    private Boolean consentPersonalInfo;

}