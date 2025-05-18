package com.pigma.harusari.user.command.service;

import com.pigma.harusari.category.command.dto.CategoryCreateRequest;
import com.pigma.harusari.category.command.entity.Category;
import com.pigma.harusari.category.command.repository.CategoryCommandRepository;
import com.pigma.harusari.user.command.dto.SignUpRequest;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserCommandRepository memberRepository;
    private final CategoryCommandRepository categoryRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(SignUpRequest request) {

        // 1. 이메일 관련 검증
        String verified = redisTemplate.opsForValue().get("EMAIL_VERIFIED:" + request.getEmail());
        if (!"true".equals(verified)) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 2. 성별 변환
        Gender gender = Gender.fromString(request.getGender());

        // 3. 회원 저장
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .gender(gender)
                .userRegisteredAt(LocalDateTime.now())
                .build();

        Member savedMember = memberRepository.save(member);

        // 4. 카테고리 자동 등록
        for (CategoryCreateRequest catReq : request.getCategoryList()) {
            categoryRepository.save(Category.builder()
                    .memberUid(savedMember.getMemberId())
                    .categoryName(catReq.getCategoryName())
                    .color(catReq.getColor() != null ? catReq.getColor() : "#111111")
                    .completionStatus(false)
                    .build());
        }

        // 5. 이메일 인증 상태 삭제
        redisTemplate.delete("EMAIL_VERIFIED:" + request.getEmail());
    }
}