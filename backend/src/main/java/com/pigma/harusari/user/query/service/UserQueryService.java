package com.pigma.harusari.user.query.service;

import com.pigma.harusari.user.query.dto.UserProfileResponse;

public interface UserQueryService {

    UserProfileResponse getUserProfile(Long userId);

}
