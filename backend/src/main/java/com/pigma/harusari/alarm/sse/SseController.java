package com.pigma.harusari.alarm.sse;

import com.pigma.harusari.common.auth.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/alarm", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
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
