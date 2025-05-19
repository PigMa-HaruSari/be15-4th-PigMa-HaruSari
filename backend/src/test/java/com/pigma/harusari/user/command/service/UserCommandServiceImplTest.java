package com.pigma.harusari.user.command.service;

import com.pigma.harusari.category.command.dto.CategoryCreateRequest;
import com.pigma.harusari.category.command.entity.Category;
import com.pigma.harusari.category.command.repository.CategoryCommandRepository;
import com.pigma.harusari.user.command.dto.SignUpRequest;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.command.exception.CategoryRequiredException;
import com.pigma.harusari.user.command.exception.EmailDuplicatedException;
import com.pigma.harusari.user.command.exception.EmailVerificationFailedException;
import com.pigma.harusari.user.command.exception.NicknameRequiredException;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[회원가입] 인증코드가 일치하지 않아 인증이 실패한 경우 예외가 발생하는 테스트")
    void testEmailVerificationFailed() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
                .categoryList(List.of(CategoryCreateRequest.builder()
                        .categoryName("운동")
                        .build()))
                .build();

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:test@example.com")).willReturn(null); // 인증 안됨

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.register(request))
                .isInstanceOf(EmailVerificationFailedException.class)
                .hasMessage("인증번호가 일치하지 않습니다.");
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
                .hasMessage("중복되는 이메일입니다.");
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
                .hasMessage("닉네임을 입력해야 합니다.");
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
                .categoryList(null) // 카테고리 누락
                .build();

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("EMAIL_VERIFIED:test@example.com")).willReturn("true");
        given(userCommandRepository.existsByEmail("test@example.com")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userCommandServiceImpl.register(request))
                .isInstanceOf(CategoryRequiredException.class)
                .hasMessage("1개 이상의 카테고리를 등록해야 합니다.");
    }

    @Test
    @DisplayName("[회원가입] 회원가입 성공 테스트")
    void testSuccessfulRegister() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
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
}