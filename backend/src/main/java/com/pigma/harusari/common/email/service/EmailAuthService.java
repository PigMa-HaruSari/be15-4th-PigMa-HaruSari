package com.pigma.harusari.common.email.service;

public interface EmailAuthService {

    void sendVerificationCode(String email);
    void verifyCode(String email, String code);

}
