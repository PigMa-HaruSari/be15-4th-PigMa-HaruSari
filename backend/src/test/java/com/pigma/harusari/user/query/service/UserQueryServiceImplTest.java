package com.pigma.harusari.user.query.service;

import com.pigma.harusari.common.auth.exception.AuthErrorCode;
import com.pigma.harusari.common.auth.exception.LogInMemberNotFoundException;
import com.pigma.harusari.user.command.entity.Gender;
import com.pigma.harusari.user.command.entity.Member;
import com.pigma.harusari.user.query.dto.UserProfileResponse;
import com.pigma.harusari.user.query.repository.UserQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("[회원 - service] UserQueryServiceImpl 테스트")
class UserQueryServiceImplTest {

    private final UserQueryRepository userQueryRepository = mock(UserQueryRepository.class);
    private final UserQueryServiceImpl userQueryService = new UserQueryServiceImpl(userQueryRepository);

    @Test
    @DisplayName("[회원 정보 조회] 회원 정보 조회 성공")
    void testGetUserProfileSuccess() {
        // given
        Long userId = 1L;
        Member mockMember = Member.builder()
                .email("test@naver.com")
                .nickname("테스트유저")
                .password("encrypted-pw")
                .gender(Gender.FEMALE)
                .consentPersonalInfo(false)
                .userRegisteredAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        when(userQueryRepository.findById(userId)).thenReturn(Optional.of(mockMember));

        // when
        UserProfileResponse response = userQueryService.getUserProfile(userId);

        // then
        assertThat(response.getEmail()).isEqualTo("test@naver.com");
        assertThat(response.getNickname()).isEqualTo("테스트유저");
        assertThat(response.getGender()).isEqualTo("FEMALE");
        assertThat(response.getConsentPersonalInfo()).isEqualTo(false);
        assertThat(response.getUserRegisteredAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("[회원 정보 조회] 존재하지 않는 회원에게 예외를 발생하는 테스트")
    void testGetUserProfileFail() {
        // given
        when(userQueryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userQueryService.getUserProfile(999L))
                .isInstanceOf(LogInMemberNotFoundException.class)
                .hasMessage(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorMessage());
    }

}