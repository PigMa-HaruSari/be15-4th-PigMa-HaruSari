package com.pigma.harusari.user.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.user.command.dto.SignUpRequest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserCommandController.class)
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

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("성이름")
                .gender("FEMALE")
                .categoryList(List.of(CategoryCreateRequest.builder().categoryName("운동").build()))
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
}
