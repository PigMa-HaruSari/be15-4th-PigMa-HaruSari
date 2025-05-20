package com.pigma.harusari.user.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserProfileResponse {

    private String email;
    private String nickname;
    private String gender;
    private LocalDateTime userRegisteredAt;

}