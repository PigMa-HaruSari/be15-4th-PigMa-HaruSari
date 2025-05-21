package com.pigma.harusari.user.command.service;


import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import com.pigma.harusari.common.auth.exception.AuthErrorCode;
import com.pigma.harusari.common.auth.exception.LogInMemberNotFoundException;
import com.pigma.harusari.user.command.dto.SignOutRequest;
import com.pigma.harusari.user.command.dto.SignUpRequest;
import com.pigma.harusari.user.command.dto.UpdatePasswordRequest;
import com.pigma.harusari.user.command.dto.UpdateUserProfileRequest;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.exception.*;
import com.pigma.harusari.user.command.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserCommandRepository memberRepository;
    private final CategoryCommandRepository categoryRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(SignUpRequest request) {
        // 1. 유효성 검증: 이메일, 닉네임, 개인정보 수집 동의, 카테고리
        String verified = redisTemplate.opsForValue().get("EMAIL_VERIFIED:" + request.getEmail());
        if (!"true".equals(verified)) {
            throw new EmailVerificationFailedException(UserCommandErrorCode.EMAIL_VERIFICATION_FAILED);
        }
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailDuplicatedException(UserCommandErrorCode.EMAIL_DUPLICATED);
        }
        if(request.getNickname() == null || request.getNickname().trim().isEmpty()){
            throw new NicknameRequiredException(UserCommandErrorCode.NICKNAME_REQUIRED);
        }
        if(request.getConsentPersonalInfo() == null || request.getConsentPersonalInfo().describeConstable().isEmpty()) {
            throw new ConsentRequiredException(UserCommandErrorCode.CONSENT_REQUIRED);
        }
        if(request.getCategoryList() == null || request.getCategoryList().isEmpty()){
            throw new CategoryRequiredException(UserCommandErrorCode.CATEGORY_REQUIRED);
        }

        // 2. 성별 변환
        Gender gender = Gender.fromString(request.getGender());

        // 3. 회원 저장
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .gender(gender)
                .consentPersonalInfo(request.getConsentPersonalInfo())
                .userRegisteredAt(LocalDateTime.now())
                .build();

        Member savedMember = memberRepository.save(member);

        // 4. 카테고리 자동 등록
        for (CategoryCreateRequest catReq : request.getCategoryList()) {
            categoryRepository.save(Category.builder()
                    .memberId(savedMember.getMemberId())
                    .categoryName(catReq.getCategoryName())
                    .color(catReq.getColor() != null ? catReq.getColor() : "#111111")
                    .completed(false)
                    .build());
        }

        // 5. 이메일 인증 상태 삭제
        redisTemplate.delete("EMAIL_VERIFIED:" + request.getEmail());
    }

    @Override
    public void updateUserProfile(Long userId, UpdateUserProfileRequest request) {
        // 1. 사용자 존재 여부 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        // 2. 업데이트 요청이 아예 비어 있는 경우
        if (request.getNickname() == null
                && request.getGender() == null
                && request.getConsentPersonalInfo() == null) {
            throw new EmptyUpdateRequestException(UserCommandErrorCode.EMPTY_UPDATE_REQUEST);
        }

        // 3. null인 경우 기존 값으로 유지
        String newNickname = request.getNickname() != null ? request.getNickname() : member.getNickname();
        Gender newGender = request.getGender() != null ? request.getGender() : member.getGender();
        Boolean newConsent = request.getConsentPersonalInfo() != null ? request.getConsentPersonalInfo() : member.getConsentPersonalInfo();

        // 4. 변경사항 반영
        member.updateProfile(newNickname, newGender, newConsent);
    }

    @Override
    public void changePassword(Long userId, UpdatePasswordRequest request) {
        // 1. 사용자 존재 여부 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        // 2. 기존 비밀번호가 일치하지 않을 때
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new CurrentPasswordIncorrectException(UserCommandErrorCode.PASSWORD_MISMATCH);
        }

        // 3. 새로 입력한 비밀번호가 확인 값과 일치하지 않을 때
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new NewPasswordMismatchException(UserCommandErrorCode.NEW_PASSWORD_MISMATCH);
        }

        // 4. 새 비밀번호가 길이 기준을 만족하지 않을 때
        if (request.getNewPassword().length() < 10 || request.getNewPassword().length() > 20) {
            throw new PasswordLengthInvalidException(UserCommandErrorCode.PASSWORD_LENGTH_INVALID);
        }

        // 5. 비밀번호 변경 완료
        member.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Override
    public void signOut(Long userId, SignOutRequest request) {
        // 1. 사용자 존재 여부 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND));

        // 2. 입력한 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CurrentPasswordIncorrectException(UserCommandErrorCode.PASSWORD_MISMATCH);
        }

        // 3. 이미 탈퇴한 회원인지 확인
        if (Boolean.TRUE.equals(member.getUserDeletedAt())) {
            throw new AlreadySignedOutMemberException(UserCommandErrorCode.ALREADY_SIGNED_OUT_MEMBER);
        }

        // 4. 회원 탈퇴
        member.signOut();
    }

}