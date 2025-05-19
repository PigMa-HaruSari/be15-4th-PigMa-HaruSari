package com.pigma.harusari.user.command.service;

import com.pigma.harusari.category.command.dto.CategoryCreateRequest;
import com.pigma.harusari.category.command.entity.Category;
import com.pigma.harusari.category.command.repository.CategoryCommandRepository;
import com.pigma.harusari.user.command.dto.SignUpRequest;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

class UserCommandServiceTest {

    @InjectMocks
    private UserCommandService userCommandService;

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
    @DisplayName("1. 중복 이메일이면 예외 발생")
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
        assertThatThrownBy(() -> userCommandService.register(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");
    }

    @Test
    @DisplayName("2. 이메일 인증이 안된 경우 예외 발생")
    void testEmailNotVerified() {
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
        assertThatThrownBy(() -> userCommandService.register(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이메일 인증이 완료되지 않았습니다.");
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
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
        userCommandService.register(request);

        // then
        verify(userCommandRepository).save(any(Member.class));
        verify(categoryCommandRepository).save(any(Category.class));
        verify(redisTemplate).delete("EMAIL_VERIFIED:test@example.com");
    }
}