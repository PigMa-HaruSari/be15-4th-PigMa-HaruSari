package com.pigma.harusari.common.auth.service;

import com.pigma.harusari.common.auth.exception.AuthErrorCode;
import com.pigma.harusari.common.auth.exception.LogInMemberNotFoundException;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCommandRepository userCommandRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = userCommandRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(member.getMemberId()),
                member.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}