package com.pigma.harusari.common.auth.service;

import com.pigma.harusari.common.auth.dto.LoginRequest;
import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.auth.dto.TokenResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    TokenResponse refreshToken(String providedRefreshToken);

    void logout(String refreshToken);

}
