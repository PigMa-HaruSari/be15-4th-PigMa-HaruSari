/*
package com.pigma.harusari.common.auth.service;

import com.pigma.harusari.common.auth.dto.LoginRequest;
import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.auth.dto.TokenResponse;
import com.pigma.harusari.common.auth.entity.RefreshToken;
import com.pigma.harusari.common.auth.exception.*;
import com.pigma.harusari.common.jwt.JwtTokenProvider;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCommandRepository userCommandRepository;
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 회원 조회하고 비밀번호 일치 여부 확인
        Member member = userCommandRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new LogInPasswordMismatchException(AuthErrorCode.LOGIN_PASSWORD_MISMATCH);
        }

        // 2. accessToken, refreshToken 발급
        String accessToken = jwtTokenProvider.createToken(
                String.valueOf(member.getMemberId()), member.getGender().name()
        );

        String refreshToken = jwtTokenProvider.createRefreshToken(
                String.valueOf(member.getMemberId()), member.getGender().name()
        );

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

    @Override
    public TokenResponse refreshToken(String providedRefreshToken) {
        // 1. refreshToken 유효성 검증
        jwtTokenProvider.validateToken(providedRefreshToken);

        // 2. Redis에 저장된 refreshToken 조회
        String userId = jwtTokenProvider.getUsernameFromJWT(providedRefreshToken);
        RefreshToken stored = redisTemplate.opsForValue().get(userId);
        if (stored == null || !stored.getToken().equals(providedRefreshToken)) {
            throw new RefreshTokenInvalidException(AuthErrorCode.REFRESH_TOKEN_INVALID);
        }

        // 3. DB에서 회원 정보 조회
        Member member = userCommandRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RefreshMemberNotFoundException(AuthErrorCode.REFRESH_MEMBER_NOT_FOUND));

        // 4. 새 accessToken, refreshToken 생성
        String accessToken = jwtTokenProvider.createToken(userId, member.getGender().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, member.getGender().name());

        // 5. Redis에 새로운 refreshToken 갱신
        redisTemplate.opsForValue().set(userId,
                RefreshToken.builder().token(refreshToken).build(),
                Duration.ofDays(7));

        // 6. 반환
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        // 1. refreshToken 유효성 검증
        jwtTokenProvider.validateToken(refreshToken);

        // 2. 사용자를 찾아 Redis에서 삭제
        String userId = jwtTokenProvider.getUsernameFromJWT(refreshToken);
        redisTemplate.delete(userId);
    }

}*/
package com.pigma.harusari.common.auth.service;

import com.pigma.harusari.common.auth.dto.LoginRequest;
import com.pigma.harusari.common.auth.dto.LoginResponse;
import com.pigma.harusari.common.auth.dto.TokenResponse;
import com.pigma.harusari.common.auth.entity.RefreshToken;
import com.pigma.harusari.common.auth.exception.*;
import com.pigma.harusari.common.jwt.JwtTokenProvider;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCommandRepository userCommandRepository;
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        Member member = userCommandRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new LogInPasswordMismatchException(AuthErrorCode.LOGIN_PASSWORD_MISMATCH);
        }

        Long memberId = member.getMemberId();
        String accessToken = jwtTokenProvider.createToken(memberId, member.getGender().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId, member.getGender().name());

        RefreshToken refreshTokenObj = RefreshToken.builder()
                .token(refreshToken)
                .build();

        redisTemplate.opsForValue().set(
                String.valueOf(memberId),
                refreshTokenObj,
                Duration.ofDays(7)
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(member.getNickname())
                .userId(memberId)
                .build();
    }

    @Override
    public TokenResponse refreshToken(String providedRefreshToken) {
        jwtTokenProvider.validateToken(providedRefreshToken);

        String userId = jwtTokenProvider.getUsernameFromJWT(providedRefreshToken);
        RefreshToken stored = redisTemplate.opsForValue().get(userId);
        if (stored == null || !stored.getToken().equals(providedRefreshToken)) {
            throw new RefreshTokenInvalidException(AuthErrorCode.REFRESH_TOKEN_INVALID);
        }

        Member member = userCommandRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RefreshMemberNotFoundException(AuthErrorCode.REFRESH_MEMBER_NOT_FOUND));

        Long memberId = member.getMemberId();
        String accessToken = jwtTokenProvider.createToken(memberId, member.getGender().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId, member.getGender().name());

        redisTemplate.opsForValue().set(
                String.valueOf(memberId),
                RefreshToken.builder().token(refreshToken).build(),
                Duration.ofDays(7)
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String userId = jwtTokenProvider.getUsernameFromJWT(refreshToken);
        redisTemplate.delete(userId);
    }
}
