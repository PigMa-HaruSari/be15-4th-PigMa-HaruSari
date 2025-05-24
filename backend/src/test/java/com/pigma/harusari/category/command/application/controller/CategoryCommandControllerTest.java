package com.pigma.harusari.category.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.category.command.application.dto.request.CategoryCreateRequest;
import com.pigma.harusari.category.command.application.dto.request.CategoryUpdateRequest;
import com.pigma.harusari.category.command.domain.service.CategoryCommandService;
import com.pigma.harusari.support.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[카테고리 - controller] CategoryCommandController 테스트")
public class CategoryCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryCommandService categoryCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockCustomUser(memberId = 1L)
    @DisplayName("[카테고리 생성 - 성공]")
    void createCategory() throws Exception {
        // given
        CategoryCreateRequest request = new CategoryCreateRequest(1L, "건강", "#FF0000");
        Mockito.when(categoryCommandService.createCategory(eq(1L), any(CategoryCreateRequest.class)))
                .thenReturn(100L); // 예시 응답 ID

        // when & then
        mockMvc.perform(post("/api/v1/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.categoryId").value(100L));
    }

    @Test
    @WithMockCustomUser(memberId = 1L)
    @DisplayName("[카테고리 수정 - 성공]")
    void updateCategory() throws Exception {
        // given
        Long categoryId = 10L;
        CategoryUpdateRequest request = new CategoryUpdateRequest("운동", "#00FF00");

        doNothing().when(categoryCommandService)
                .updateCategory(eq(categoryId), eq(1L), any(CategoryUpdateRequest.class));

        // when & then
        mockMvc.perform(put("/api/v1/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockCustomUser(memberId = 1L)
    @DisplayName("[카테고리 완료 처리 - 성공]")
    void completeCategory() throws Exception {
        // given
        Long categoryId = 10L;

        doNothing().when(categoryCommandService).completeCategory(categoryId, 1L);

        // when & then
        mockMvc.perform(put("/api/v1/categories/{categoryId}/complete", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockCustomUser(memberId = 1L)
    @DisplayName("[카테고리 삭제 - 성공]")
    void deleteCategory() throws Exception {
        // given
        Long categoryId = 10L;
        String confirmText = "삭제";
        boolean hasSchedules = false;

        doNothing().when(categoryCommandService)
                .deleteCategory(categoryId, 1L, confirmText, hasSchedules);

        // when & then
        mockMvc.perform(delete("/api/v1/categories/{categoryId}", categoryId)
                        .param("confirmText", confirmText)
                        .param("hasSchedules", String.valueOf(hasSchedules)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
