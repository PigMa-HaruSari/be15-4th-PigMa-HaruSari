package com.pigma.harusari.user.command.service;

import com.pigma.harusari.user.command.dto.*;

public interface UserCommandService {

    void register(SignUpRequest request);

    void updateUserProfile(Long userId, UpdateUserProfileRequest request);

    void changePassword(Long userId, UpdatePasswordRequest request);

    void signOut(Long userId, SignOutRequest request);

    void resetPassword(ResetPasswordPerformRequest request);

}
