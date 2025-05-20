package com.pigma.harusari.alarm.sse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SseController.class)
@DisplayName("[SSE - controller] SseController 테스트")
class SseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SseService sseService;

    @Test
    @WithMockUser(username = "1")
    @DisplayName("[SSE 구독] SSE 구독 성공 테스트")
    void testSubscribeSuccess() throws Exception {
        // given
        SseEmitter emitter = new SseEmitter();
        when(sseService.subscribe(anyLong())).thenReturn(emitter);

        // when & then
        mockMvc.perform(get("/api/v1/alarm")
                        .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(status().isOk());
    }
}
