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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("[회원 - service] UserCommandServiceImpl 테스트")
class UserCommandServiceImplTest {

    @InjectMocks
    private UserCommandServiceImpl userCommandServiceImpl;

    @Mock
    private UserCommandRepository userCommandRepository;

    @Mock
    private CategoryCommandRepository categoryCommandRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final String rawPassword = "pastPassword123!";
    private final String encodedPassword = "$2a$10$1234567890123456789012";
    private final String newSecurePassword = "changedPassword456@";
    private final String newEncodedPassword = "$2a$10$98765432109876543210987";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("[회원가입] 회원가입 요청 성공 테스트")
    void testSuccessfulRegister() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
                .consentPersonalInfo(true)
                .categoryList(List.of(CategoryCreateRequest.builder()
                        .categoryName("운동")
                        .build()))
                .build();

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:test@example.com")).willReturn("true");
        given(userCommandRepository.existsByEmail("test@example.com")).willReturn(false);
        given(passwordEncoder.encode("securePassword")).willReturn("encodedPW");

        // 빌더로 객체 생성 후 memberId 주입
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성+이름")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);
        given(userCommandRepository.save(any(Member.class))).willReturn(member);

        // when
        userCommandServiceImpl.register(request);

        // then
        verify(userCommandRepository).save(any(Member.class));
        verify(categoryCommandRepository).save(any(Category.class));
        verify(redisTemplate).delete("EMAIL_VERIFIED:test@example.com");
    }

    @Test
    @DisplayName("[회원가입] 인증코드가 일치하지 않아 이메일 인증이 실패한 경우 예외가 발생하는 테스트")
    void testEmailVerificationFailed() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
                .consentPersonalInfo(true)
                .categoryList(List.of(CategoryCreateRequest.builder()
                        .categoryName("운동")
                        .build()))
                .build();

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:test@example.com")).willReturn(null); // 인증 안됨

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.register(request))
                .isInstanceOf(EmailVerificationFailedException.class)
                .hasMessage(UserCommandErrorCode.EMAIL_VERIFICATION_FAILED.getErrorMessage());
    }

    @Test
    @DisplayName("[회원가입] 이메일이 중복될 때 예외가 발생하는 테스트")
    void testEmailDuplicate() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
                .consentPersonalInfo(true)
                .categoryList(List.of(CategoryCreateRequest.builder()
                        .categoryName("운동")
                        .build()))
                .build();

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:test@example.com")).willReturn("true");
        given(userCommandRepository.existsByEmail("test@example.com")).willReturn(true); // 중복 이메일

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.register(request))
                .isInstanceOf(EmailDuplicatedException.class)
                .hasMessage(UserCommandErrorCode.EMAIL_DUPLICATED.getErrorMessage());
    }

    @Test
    @DisplayName("[회원가입] 닉네임을 입력하지 않았을 때 예외가 발생하는 테스트")
    void testNicknameRequired(){
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname(null) // 닉네임 누락
                .gender("FEMALE")
                .consentPersonalInfo(true)
                .categoryList(List.of(CategoryCreateRequest.builder()
                        .categoryName("운동")
                        .build()))
                .build();

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:test@example.com")).willReturn("true");
        given(userCommandRepository.existsByEmail("test@example.com")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.register(request))
                .isInstanceOf(NicknameRequiredException.class)
                .hasMessage(UserCommandErrorCode.NICKNAME_REQUIRED.getErrorMessage());
    }

    @Test
    @DisplayName("[회원가입] 개인정보 수집 동의가 누락되었을 때 예외가 발생하는 테스트")
    void testConsentRequired() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
                .categoryList(List.of(CategoryCreateRequest.builder().categoryName("운동").build()))
                .consentPersonalInfo(null) // 동의 누락
                .build();

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:test@example.com")).willReturn("true");
        given(userCommandRepository.existsByEmail("test@example.com")).willReturn(false);


        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.register(request))
                .isInstanceOf(ConsentRequiredException.class)
                .hasMessage(UserCommandErrorCode.CONSENT_REQUIRED.getErrorMessage());
    }

    @Test
    @DisplayName("[회원가입] 카테고리를 선택하지 않았을 때 예외가 발생하는 테스트")
    void testCategoryRequired(){
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
                .consentPersonalInfo(true)
                .categoryList(null) // 카테고리 누락
                .build();

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:test@example.com")).willReturn("true");
        given(userCommandRepository.existsByEmail("test@example.com")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.register(request))
                .isInstanceOf(CategoryRequiredException.class)
                .hasMessage(UserCommandErrorCode.CATEGORY_REQUIRED.getErrorMessage());
    }

    @Test
    @DisplayName("[개인정보 수정] 정상 수정 완료 테스트")
    void testUpdateProfileSuccess() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("기존닉네임")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(false)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", memberId);
        given(userCommandRepository.findById(memberId)).willReturn(Optional.of(member));

        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
                .nickname("새닉네임")
                .gender(Gender.MALE)
                .consentPersonalInfo(true)
                .build();

        // when
        userCommandServiceImpl.updateUserProfile(memberId, request);

        // then
        assertThat(member.getNickname()).isEqualTo("새닉네임");
        assertThat(member.getGender()).isEqualTo(Gender.MALE);
        assertThat(member.getConsentPersonalInfo()).isTrue();
    }

    @Test
    @DisplayName("[개인정보 수정] 사용자 정보가 잘못된 경우에 예외가 발생하는 테스트")
    void testUpdateProfileUserNotFound() {
        // given
        Long memberId = 999L;
        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
                .nickname("변경닉네임")
                .gender(Gender.MALE)
                .consentPersonalInfo(true)
                .build();

        given(userCommandRepository.findById(memberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.updateUserProfile(memberId, request))
                .isInstanceOf(LogInMemberNotFoundException.class)
                .hasMessage(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("[개인정보 수정] 수정 요청이 비어있는 경우에 예외가 발생하는 테스트")
    void testUpdateProfileEmptyRequest() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("기존닉네임")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(false)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", memberId);
        given(userCommandRepository.findById(memberId)).willReturn(Optional.of(member));

        UpdateUserProfileRequest emptyRequest = UpdateUserProfileRequest.builder()
                .nickname(null)
                .gender(null)
                .consentPersonalInfo(null)
                .build();

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.updateUserProfile(memberId, emptyRequest))
                .isInstanceOf(EmptyUpdateRequestException.class)
                .hasMessage(UserCommandErrorCode.EMPTY_UPDATE_REQUEST.getErrorMessage());
    }

    @Test
    @DisplayName("[비밀번호 변경] 비밀번호 변경 요청 성공 테스트")
    void testChangePasswordSuccess() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .nickname("테스트")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);

        given(userCommandRepository.findById(1L)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
        given(passwordEncoder.encode(newSecurePassword)).willReturn(newEncodedPassword);

        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword(rawPassword)
                .newPassword(newSecurePassword)
                .confirmPassword(newSecurePassword)
                .build();

        // when
        userCommandServiceImpl.changePassword(1L, request);

        // then
        assertThat(member.getPassword()).isEqualTo(newEncodedPassword);
    }

    @Test
    @DisplayName("[비밀번호 변경] 기존 비밀번호가 틀린 경우 예외 발생 테스트")
    void testChangePasswordFail_dueToWrongCurrentPassword() {
        Member member = Member.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .nickname("테스트")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);

        given(userCommandRepository.findById(1L)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(false);

        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword(rawPassword)
                .newPassword(newSecurePassword)
                .confirmPassword(newSecurePassword)
                .build();

        assertThatThrownBy(() -> userCommandServiceImpl.changePassword(1L, request))
                .isInstanceOf(CurrentPasswordIncorrectException.class)
                .hasMessage(UserCommandErrorCode.PASSWORD_MISMATCH.getErrorMessage());
    }

    @Test
    @DisplayName("[비밀번호 변경] 새 비밀번호와 확인 값이 일치하지 않는 경우 예외 발생 테스트")
    void testChangePasswordFail_dueToMismatchNewPassword() {
        Member member = Member.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .nickname("테스트")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);

        given(userCommandRepository.findById(1L)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword(rawPassword)
                .newPassword(newSecurePassword)
                .confirmPassword("differentPass765@")
                .build();

        assertThatThrownBy(() -> userCommandServiceImpl.changePassword(1L, request))
                .isInstanceOf(NewPasswordMismatchException.class)
                .hasMessage(UserCommandErrorCode.NEW_PASSWORD_MISMATCH.getErrorMessage());
    }

    @Test
    @DisplayName("[비밀번호 변경] 새 비밀번호가 길이 규칙을 위반한 경우 예외 발생 테스트")
    void testChangePasswordFail_dueToInvalidLength() {
        Member member = Member.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .nickname("테스트")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);

        given(userCommandRepository.findById(1L)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword(rawPassword)
                .newPassword("short")
                .confirmPassword("short")
                .build();

        assertThatThrownBy(() -> userCommandServiceImpl.changePassword(1L, request))
                .isInstanceOf(PasswordLengthInvalidException.class)
                .hasMessage(UserCommandErrorCode.PASSWORD_LENGTH_INVALID.getErrorMessage());
    }

    @Test
    @DisplayName("[회원탈퇴] 회원탈퇴 성공 테스트")
    void testSignOutSuccess() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .nickname("닉네임")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);

        SignOutRequest request = SignOutRequest.builder().password(rawPassword).build();

        given(userCommandRepository.findById(1L)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

        // when
        userCommandServiceImpl.signOut(1L, request);

        // then
        assertThat(member.getUserDeletedAt()).isTrue();
    }

    @Test
    @DisplayName("[회원탈퇴] 존재하지 않는 사용자에 대해 예외 발생하는 테스트")
    void testSignOutUserNotFound() {
        // given
        SignOutRequest request = SignOutRequest.builder().password(rawPassword).build();
        given(userCommandRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.signOut(999L, request))
                .isInstanceOf(LogInMemberNotFoundException.class)
                .hasMessage(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("[회원탈퇴] 비밀번호 불일치로 인해 예외 발생하는 테스트")
    void testSignOutPasswordMismatch() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .nickname("닉네임")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);

        SignOutRequest request = SignOutRequest.builder().password("wrongPassword").build();

        given(userCommandRepository.findById(1L)).willReturn(Optional.of(member));
        given(passwordEncoder.matches("wrongPassword", encodedPassword)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.signOut(1L, request))
                .isInstanceOf(CurrentPasswordIncorrectException.class)
                .hasMessage(UserCommandErrorCode.PASSWORD_MISMATCH.getErrorMessage());
    }

    @Test
    @DisplayName("[회원탈퇴] 이미 탈퇴한 사용자에 대해 예외 발생하는 테스트")
    void testSignOutAlreadySignedOut() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .nickname("닉네임")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(true)
                .userRegisteredAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(member, "memberId", 1L);
        ReflectionTestUtils.setField(member, "userDeletedAt", true);

        SignOutRequest request = SignOutRequest.builder().password(rawPassword).build();

        given(userCommandRepository.findById(1L)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.signOut(1L, request))
                .isInstanceOf(AlreadySignedOutMemberException.class)
                .hasMessage(UserCommandErrorCode.ALREADY_SIGNED_OUT_MEMBER.getErrorMessage());
    }

}