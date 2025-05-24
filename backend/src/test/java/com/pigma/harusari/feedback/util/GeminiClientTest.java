package com.pigma.harusari.feedback.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("[FeedbackUtil] GeminiClient 테스트")
class GeminiClientTest {

    @Test
    @DisplayName("generateFeedback - 성공 시 AI 응답 반환")
    void testGenerateFeedback_success() {
        // given
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        doReturn(bodySpec).when(bodySpec).bodyValue(any(Map.class));
        when(bodySpec.retrieve()).thenReturn(responseSpec);

        // Mocked AI response map 구조
        Map<String, Object> mockResponse = Map.of(
                "candidates", List.of(
                        Map.of(
                                "content", Map.of(
                                        "parts", List.of("테스트 AI 응답")
                                )
                        )
                )
        );

        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        GeminiClient client = new GeminiClient(webClientBuilder);
        // 필드 주입 대신 reflection or setter로 apiKey, baseUrl 설정 가능
        setField(client, "apiKey", "dummy-api-key");
        setField(client, "baseUrl", "http://dummy-url");

        // when
        String result = client.generateFeedback("테스트 프롬프트");

        // then
        assertThat(result).isEqualTo("테스트 AI 응답");

        // verify 호출 흐름 확인도 가능
        verify(webClientBuilder).build();
        verify(webClient).post();
        verify(uriSpec).uri(contains("dummy-api-key"));
        verify(bodySpec).bodyValue(any());
        verify(bodySpec).retrieve();
        verify(responseSpec).bodyToMono(Map.class);
    }

    @Test
    @DisplayName("generateFeedback - 에러 시 기본 에러 메시지 반환")
    void testGenerateFeedback_error() {
        // given
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        doReturn(bodySpec).when(bodySpec).bodyValue(any(Map.class));
        when(bodySpec.retrieve()).thenReturn(responseSpec);

        // 에러 발생하는 Mono 반환
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new RuntimeException("네트워크 오류")));

        GeminiClient client = new GeminiClient(webClientBuilder);
        setField(client, "apiKey", "dummy-api-key");
        setField(client, "baseUrl", "http://dummy-url");

        // when
        String result = client.generateFeedback("테스트 프롬프트");

        // then
        assertThat(result).isEqualTo("AI 피드백 생성 중 오류가 발생했습니다.");
    }

    // 리플렉션으로 private 필드 값 세팅하는 유틸 메서드
    private static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
