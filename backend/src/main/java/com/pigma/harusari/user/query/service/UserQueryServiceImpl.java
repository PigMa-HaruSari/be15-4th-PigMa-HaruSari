package com.pigma.harusari.user.query.service;

import com.pigma.harusari.common.auth.exception.AuthErrorCode;
import com.pigma.harusari.common.auth.exception.LogInMemberNotFoundException;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.query.dto.UserProfileResponse;
import com.pigma.harusari.user.query.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserQueryRepository userQueryRepository;

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        Member member = userQueryRepository.findById(userId)
                .orElseThrow(() -> new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        return UserProfileResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender().name())
                .consentPersonalInfo(member.getConsentPersonalInfo())
                .userRegisteredAt(member.getUserRegisteredAt())
                .build();
    }

}