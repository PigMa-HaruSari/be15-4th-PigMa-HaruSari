package com.pigma.harusari.user.command.service;

import com.pigma.harusari.user.command.dto.SignOutRequest;
import com.pigma.harusari.user.command.dto.SignUpRequest;
import com.pigma.harusari.user.command.dto.UpdatePasswordRequest;
import com.pigma.harusari.user.command.dto.UpdateUserProfileRequest;

public interface UserCommandService {

    void register(SignUpRequest request);

    void updateUserProfile(Long userId, UpdateUserProfileRequest request);

    void changePassword(Long userId, UpdatePasswordRequest request);

    void signOut(Long userId, SignOutRequest request);

}
