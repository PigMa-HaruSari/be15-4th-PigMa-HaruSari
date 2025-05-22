package com.pigma.harusari.feedback.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.base-url}")
    private String baseUrl;

    @Value("${spring.ai.openai.chat.completions-path}")
    private String completionsPath;

    private final WebClient.Builder webClientBuilder;

    public String generateFeedback(String prompt) {
        Map<String, Object> body = Map.of(
                "model", "gemini-2.0-flash-lite",
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        return webClientBuilder.build()
                .post()
                .uri(baseUrl + completionsPath + "?key=" + apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> ((Map)((List) resp.get("choices")).get(0)).get("message").toString())
                .onErrorReturn("AI 피드백 생성 중 오류가 발생했습니다.")
                .block();
    }
}
