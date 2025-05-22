package com.pigma.harusari.feedback.command.service;

public interface FeedbackCommandService {
    void generateMonthlyFeedbackForAllUsers();
    void generateMonthlyFeedbackForMember(Long memberId);
}
