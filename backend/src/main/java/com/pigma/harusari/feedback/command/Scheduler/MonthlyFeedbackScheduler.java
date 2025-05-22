package com.pigma.harusari.feedback.command.Scheduler;

import com.pigma.harusari.feedback.command.service.FeedbackCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MonthlyFeedbackScheduler {

    private final FeedbackCommandService feedbackCommandService;

    @Scheduled(cron = "0 0 8 1 * ?")    // 매달 1일 8시
    public void generateMonthlyFeedback() {
        log.info("[MonthlyFeedbackScheduler] Starting monthly feedback generation...");
        feedbackCommandService.generateMonthlyFeedbackForAllUsers();
    }
}
