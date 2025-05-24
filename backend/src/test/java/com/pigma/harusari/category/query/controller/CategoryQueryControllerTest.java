package com.pigma.harusari.category.query.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import com.pigma.harusari.category.query.service.CategoryQueryService;
import com.pigma.harusari.common.auth.model.CustomUserDetails;
import com.pigma.harusari.user.command.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryQueryController.class)
@DisplayName("[카테고리 - controller] CategoryQueryControllerTest 테스트")
class CategoryQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryQueryService categoryQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("내 카테고리 목록 조회 성공 테스트")
    void testGetMyCategoriesSuccess() throws Exception {
        // given
        Member mockMember = Member.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        ReflectionTestUtils.setField(mockMember, "memberId", 1L);

        CustomUserDetails userDetails = new CustomUserDetails(mockMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        List<CategoryResponse> mockCategories = List.of(
                new CategoryResponse(100L, "공부", "#123456", false),
                new CategoryResponse(101L, "운동", "#654321", true)
        );

        when(categoryQueryService.findCategoriesByMember(1L)).thenReturn(mockCategories);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/categories")
                .with(authentication(authentication))
                .accept(APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].categoryId").value(100))
                .andExpect(jsonPath("$.data[0].categoryName").value("공부"))
                .andExpect(jsonPath("$.data[1].completed").value(true));
    }
}
