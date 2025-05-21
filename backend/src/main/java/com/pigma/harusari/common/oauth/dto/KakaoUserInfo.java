package com.pigma.harusari.common.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoUserInfo {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakao_account;

    @Getter
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        public static class Profile {
            private String nickname;
        }
    }
}
