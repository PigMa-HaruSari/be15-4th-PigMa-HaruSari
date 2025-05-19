package com.pigma.harusari.user.command.service;

import com.pigma.harusari.user.command.dto.SignUpRequest;

public interface UserCommandService {

    void register(SignUpRequest request);
}
