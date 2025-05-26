package com.pigma.harusari.alarm.sse;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "SSE 알림 API", description = "서버-전송 이벤트(SSE)를 통한 알림 구독 API")
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/alarm", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(
            summary = "알림 SSE 구독",
            description = "로그인한 사용자의 알림 SSE 구독을 시작한다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "SSE 구독 성공, 알림 스트림 전송 시작"
    )
    public SseEmitter subscribe(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        System.out.println("🔔 [SSE] 연결 시도");

        if (userDetails == null) {
            System.out.println("❌ [SSE] 인증 실패 - userDetails is null");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // userDetails에서 memberId(즉 userId) 추출
        Long memberId = userDetails.getMemberId();
        System.out.println("✅ [SSE] 인증 성공 - memberId = " + memberId);

        return sseService.subscribe(memberId);
    }

}
