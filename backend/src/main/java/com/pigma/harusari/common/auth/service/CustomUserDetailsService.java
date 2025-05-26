package com.pigma.harusari.common.auth.service;

import com.pigma.harusari.common.auth.exception.AuthErrorCode;
import com.pigma.harusari.common.auth.exception.LogInMemberNotFoundException;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCommandRepository userCommandRepository;

    @Cacheable(value = "userDetailsCache", key = "#userId")
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println("📦 DB에서 사용자 조회: userId = " + userId); // 캐시 동작 확인용
        Member member = userCommandRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        return new CustomUserDetails(member);
    }
}