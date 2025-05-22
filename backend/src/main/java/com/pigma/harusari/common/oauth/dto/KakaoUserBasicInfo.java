package com.pigma.harusari.common.oauth.dto;

import lombok.Getter;

@Getter
public class KakaoUserBasicInfo {

    private String email;
    private String nickname;

    public KakaoUserBasicInfo(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

}