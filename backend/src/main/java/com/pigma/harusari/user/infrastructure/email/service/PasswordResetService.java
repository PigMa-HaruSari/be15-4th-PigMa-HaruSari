package com.pigma.harusari.user.infrastructure.email.service;

public interface PasswordResetService {

    void sendResetLink(String email);

}
