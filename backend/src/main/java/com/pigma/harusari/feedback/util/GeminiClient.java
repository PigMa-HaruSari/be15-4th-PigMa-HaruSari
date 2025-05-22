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

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.base-url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;

    public String generateFeedback(String prompt) {
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        return webClientBuilder.build()
                .post()
                .uri(baseUrl + "?key=" + apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> {
                    List candidates = (List) resp.get("candidates");
                    if (candidates != null && !candidates.isEmpty()) {
                        Map candidate = (Map) candidates.get(0);
                        Map content = (Map) candidate.get("content");
                        List parts = (List) content.get("parts");
                        if (parts != null && !parts.isEmpty()) {
                            return parts.get(0).toString(); // 또는 .get("text")로 더 깔끔하게 출력 가능
                        }
                    }
                    return "AI 응답을 해석할 수 없습니다.";
                })
                .onErrorReturn("AI 피드백 생성 중 오류가 발생했습니다.")
                .block();
    }
}
