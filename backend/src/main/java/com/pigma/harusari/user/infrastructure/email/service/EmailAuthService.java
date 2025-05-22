package com.pigma.harusari.user.infrastructure.email.service;

public interface EmailAuthService {

    void sendVerificationCode(String email);
    void verifyCode(String email, String code);

}
