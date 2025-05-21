package com.pigma.harusari.user.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.common.auth.exception.AuthErrorCode;
import com.pigma.harusari.common.auth.exception.LogInMemberNotFoundException;
import com.pigma.harusari.support.WithMockCustomUser;
import com.pigma.harusari.user.command.dto.SignOutRequest;
import com.pigma.harusari.user.command.dto.SignUpRequest;
import com.pigma.harusari.user.command.dto.UpdatePasswordRequest;
import com.pigma.harusari.user.command.dto.UpdateUserProfileRequest;
import com.pigma.harusari.user.command.exception.*;
import com.pigma.harusari.user.command.exception.handler.UserCommandExceptionHandler;
import com.pigma.harusari.user.command.service.UserCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserCommandController.class)
@Import(UserCommandExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[회원 - controller] UserCommandController 테스트")
class UserCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserCommandService userCommandService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserCommandExceptionHandler userCommandExceptionHandler;

    SignUpRequest signUpRequest;
    UpdateUserProfileRequest updateRequest;
    UpdatePasswordRequest updatePasswordRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
                .consentPersonalInfo(true)
                .categoryList(List.of(CategoryCreateRequest.builder().categoryName("운동").build()))
                .build();

        updateRequest = UpdateUserProfileRequest.builder()
                .nickname("변경된닉네임")
                .gender(null)
                .consentPersonalInfo(true)
                .build();

        updatePasswordRequest = UpdatePasswordRequest.builder()
                .currentPassword("pastPassword123!")
                .newPassword("changedPassword456@")
                .confirmPassword("changedPassword456@")
                .build();

    }

    @Test
    @DisplayName("[회원가입] 회원가입 요청 성공 테스트")
    void testSignUpSuccess() throws Exception {
        doNothing().when(userCommandService).register(signUpRequest);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[회원가입] 인증코드가 일치하지 않아 이메일 인증이 실패한 경우 예외가 발생하는 테스트")
    void testEmailVerificationFailed() throws Exception {
        doThrow(new EmailVerificationFailedException(UserCommandErrorCode.EMAIL_VERIFICATION_FAILED)).when(userCommandService).register(any());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.EMAIL_VERIFICATION_FAILED.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(UserCommandErrorCode.EMAIL_VERIFICATION_FAILED.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[회원가입] 이메일이 중복될 때 예외가 발생하는 테스트")
    void testEmailDuplicated() throws Exception {
        doThrow(new EmailDuplicatedException(UserCommandErrorCode.EMAIL_DUPLICATED)).when(userCommandService).register(any());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.EMAIL_DUPLICATED.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(UserCommandErrorCode.EMAIL_DUPLICATED.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[회원가입] 닉네임을 입력하지 않았을 때 예외가 발생하는 테스트")
    void testNicknameMissing() throws Exception {
        doThrow(new NicknameRequiredException(UserCommandErrorCode.NICKNAME_REQUIRED)).when(userCommandService).register(any());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.NICKNAME_REQUIRED.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(UserCommandErrorCode.NICKNAME_REQUIRED.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[회원가입] 카테고리를 선택하지 않았을 때 예외가 발생하는 테스트")
    void testCategoryMissing() throws Exception {
        doThrow(new CategoryRequiredException(UserCommandErrorCode.CATEGORY_REQUIRED)).when(userCommandService).register(any());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.CATEGORY_REQUIRED.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(UserCommandErrorCode.CATEGORY_REQUIRED.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[개인정보 수정] 정상 수정 완료 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testUpdateSuccess() throws Exception {
        mockMvc.perform(put("/api/v1/users/mypage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[개인정보 수정] 사용자 정보가 잘못된 경우에 예외가 발생하는 테스트")
    @WithMockCustomUser(memberId = 999L)
    void testUserNotFound() throws Exception {
        doThrow(new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND))
                .when(userCommandService).updateUserProfile(anyLong(), any());

        mockMvc.perform(put("/api/v1/users/mypage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[개인정보 수정] 수정 요청이 비어있는 경우에 예외가 발생하는 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testEmptyUpdateRequest() throws Exception {
        UpdateUserProfileRequest emptyRequest = UpdateUserProfileRequest.builder()
                .nickname(null)
                .gender(null)
                .consentPersonalInfo(null)
                .build();

        doThrow(new EmptyUpdateRequestException(UserCommandErrorCode.EMPTY_UPDATE_REQUEST)).when(userCommandService).updateUserProfile(eq(1L), any());

        mockMvc.perform(put("/api/v1/users/mypage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").exists())
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.EMPTY_UPDATE_REQUEST.getErrorCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(UserCommandErrorCode.EMPTY_UPDATE_REQUEST.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[비밀번호 변경] 비밀번호 변경 요청 성공 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testChangePasswordSuccess() throws Exception {
        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[비밀번호 변경] 기존 비밀번호가 틀린 경우 예외 발생 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testIncorrectCurrentPassword() throws Exception {
        doThrow(new CurrentPasswordIncorrectException(UserCommandErrorCode.PASSWORD_MISMATCH))
                .when(userCommandService).changePassword(eq(1L), any());

        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.PASSWORD_MISMATCH.getErrorCode()))
                .andExpect(jsonPath("$.message").value(UserCommandErrorCode.PASSWORD_MISMATCH.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[비밀번호 변경] 새 비밀번호와 확인 값이 일치하지 않는 경우 예외 발생 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testNewPasswordMismatch() throws Exception {
        doThrow(new NewPasswordMismatchException(UserCommandErrorCode.NEW_PASSWORD_MISMATCH))
                .when(userCommandService).changePassword(eq(1L), any());

        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.NEW_PASSWORD_MISMATCH.getErrorCode()))
                .andExpect(jsonPath("$.message").value(UserCommandErrorCode.NEW_PASSWORD_MISMATCH.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[비밀번호 변경] 새 비밀번호가 길이 규칙을 위반한 경우 예외 발생 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testNewPasswordLengthInvalid() throws Exception {
        doThrow(new PasswordLengthInvalidException(UserCommandErrorCode.PASSWORD_LENGTH_INVALID))
                .when(userCommandService).changePassword(eq(1L), any());

        mockMvc.perform(put("/api/v1/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.PASSWORD_LENGTH_INVALID.getErrorCode()))
                .andExpect(jsonPath("$.message").value(UserCommandErrorCode.PASSWORD_LENGTH_INVALID.getErrorMessage()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("[회원탈퇴] 회원탈퇴 성공 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testSignOutSuccess() throws Exception {
        SignOutRequest request = SignOutRequest.builder().password("password123").build();

        mockMvc.perform(put("/api/v1/users/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    @DisplayName("[회원탈퇴] 존재하지 않는 사용자에 대해 예외 발생하는 테스트")
    @WithMockCustomUser(memberId = 999L)
    void testSignOutUserNotFound() throws Exception {
        SignOutRequest request = SignOutRequest.builder().password("password123").build();
        doThrow(new LogInMemberNotFoundException(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND))
                .when(userCommandService).signOut(eq(1L), any(SignOutRequest.class));

        mockMvc.perform(put("/api/v1/users/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(AuthErrorCode.LOGIN_MEMBER_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[회원탈퇴] 비밀번호 불일치로 인해 예외 발생하는 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testSignOutPasswordMismatch() throws Exception {
        SignOutRequest request = SignOutRequest.builder().password("wrongPassword").build();
        doThrow(new CurrentPasswordIncorrectException(UserCommandErrorCode.PASSWORD_MISMATCH))
                .when(userCommandService).signOut(eq(1L), any());

        mockMvc.perform(put("/api/v1/users/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.PASSWORD_MISMATCH.getErrorCode()));
    }

    @Test
    @DisplayName("[회원탈퇴] 이미 탈퇴한 사용자에 대해 예외 발생하는 테스트")
    @WithMockCustomUser(memberId = 1L)
    void testSignOutAlreadySignedOut() throws Exception {
        SignOutRequest request = SignOutRequest.builder().password("password123").build();
        doThrow(new AlreadySignedOutMemberException(UserCommandErrorCode.ALREADY_SIGNED_OUT_MEMBER))
                .when(userCommandService).signOut(eq(1L), any());

        mockMvc.perform(put("/api/v1/users/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(UserCommandErrorCode.ALREADY_SIGNED_OUT_MEMBER.getErrorCode()));
    }

}
