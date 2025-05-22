package com.pigma.harusari.user.command.dto;

import com.pigma.harusari.user.command.entity.Gender;
import lombok.*;

@Getter
@Builder
public class UpdateUserProfileRequest {

    private String nickname;
    private Gender gender;
    private Boolean consentPersonalInfo;

}
