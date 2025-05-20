package com.pigma.harusari.user.command.service;

import com.pigma.harusari.user.command.dto.SignUpRequest;
import com.pigma.harusari.user.command.dto.UpdateUserProfileRequest;

public interface UserCommandService {

    void register(SignUpRequest request);

    void updateUserProfile(Long userId, UpdateUserProfileRequest request);

}
