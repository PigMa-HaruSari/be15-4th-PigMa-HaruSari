package com.pigma.harusari.common.auth.service;

import com.pigma.harusari.common.auth.dto.LoginRequest;
import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.auth.entity.RefreshToken;
import com.pigma.harusari.common.jwt.JwtTokenProvider;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCommandRepository userCommandRepository;
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        // 1. 회원 조회하고 비밀번호 일치 여부 확인
        Member member = userCommandRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("이메일 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // 2. accessToken, refreshToken 발급
        String accessToken = jwtTokenProvider.createToken(
                String.valueOf(member.getMemberId()), member.getGender().name());

        String refreshToken = jwtTokenProvider.createRefreshToken(
                String.valueOf(member.getMemberId()), member.getGender().name());

        // 3. refreshToken을 Redis에 저장 (key: memberId, value: refreshToken)
        RefreshToken refreshTokenObj = RefreshToken.builder()
                .token(refreshToken)
                .build();

        redisTemplate.opsForValue().set(
                String.valueOf(member.getMemberId()),
                refreshTokenObj,
                Duration.ofDays(7)
        );

        // 4. 반환
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(member.getNickname())
                .userId(member.getMemberId())
                .build();
    }

}