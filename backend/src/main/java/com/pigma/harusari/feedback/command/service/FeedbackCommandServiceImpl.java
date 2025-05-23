package com.pigma.harusari.feedback.command.service;

import com.pigma.harusari.diary.query.mapper.DiaryQueryMapper;
import com.pigma.harusari.feedback.command.entity.Feedback;
import com.pigma.harusari.feedback.command.repository.FeedbackRepository;
import com.pigma.harusari.feedback.exception.FeedbackErrorCode;
import com.pigma.harusari.feedback.exception.FeedbackException;
import com.pigma.harusari.feedback.util.FeedbackPromptBuilder;
import com.pigma.harusari.feedback.util.GeminiClient;
import com.pigma.harusari.task.schedule.query.mapper.TaskScheduleQueryMapper;
import com.pigma.harusari.user.query.mapper.UserQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackCommandServiceImpl implements FeedbackCommandService {

    private final DiaryQueryMapper diaryQueryMapper;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackPromptBuilder promptBuilder;
    private final GeminiClient geminiClient;
    private final UserQueryMapper userQueryMapper;
    private final TaskScheduleQueryMapper scheduleQueryMapper;

    @Override
    @Transactional
    public void generateMonthlyFeedbackForAllUsers() {
        List<Long> memberIds = userQueryMapper.getAllActiveUserIds();

        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate start = lastMonth.atDay(1);
        LocalDate end = lastMonth.atEndOfMonth();

        for (Long memberId : memberIds) {
            var diaries = diaryQueryMapper.getLastMonthDiaries(memberId, start, end);
            var schedules = scheduleQueryMapper.getLastMonthSchedules(memberId, start, end);

            // 일기나 스케줄이 각각 5개 이상이어야 피드백 생성
            if ((diaries == null || diaries.size() < 5) && (schedules == null || schedules.size() < 5)) {
                System.out.printf("회원 ID %d: 피드백을 생성할 수 없습니다 - 일기와 스케줄이 5개 미만%n", memberId);
                continue;
            }

            int completed = scheduleQueryMapper.countCompletedSchedules(memberId, start, end);
            double achievementRate = schedules.isEmpty() ? 0 : (double) completed / schedules.size();

            String prompt = promptBuilder.buildPrompt(diaries, schedules, achievementRate);
            String content;

            try {
                content = geminiClient.generateFeedback(prompt);
            } catch (Exception e) {
                System.err.printf("회원 ID %d: Gemini 응답 실패 - %s%n", memberId, e.getMessage());
                throw new FeedbackException(FeedbackErrorCode.GEMINI_API_ERROR);
            }

            feedbackRepository.save(Feedback.builder()
                    .memberId(memberId)
                    .feedbackContent(content)
                    .feedbackDate(new Date())
                    .build());
        }
    }

    @Override
    @Transactional
    public void generateMonthlyFeedbackForMember(Long memberId) {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate start = lastMonth.atDay(1);
        LocalDate end = lastMonth.atEndOfMonth();

        var diaries = diaryQueryMapper.getLastMonthDiaries(memberId, start, end);
        var schedules = scheduleQueryMapper.getLastMonthSchedules(memberId, start, end);

        if ((diaries == null || diaries.size() < 5) && (schedules == null || schedules.size() < 5)) {
            throw new FeedbackException(FeedbackErrorCode.INSUFFICIENT_DATA);
        }

        int completed = scheduleQueryMapper.countCompletedSchedules(memberId, start, end);
        double achievementRate = schedules.isEmpty() ? 0 : (double) completed / schedules.size();

        String prompt = promptBuilder.buildPrompt(diaries, schedules, achievementRate);
        String content;

        try {
            content = geminiClient.generateFeedback(prompt);
        } catch (Exception e) {
            throw new FeedbackException(FeedbackErrorCode.GEMINI_API_ERROR);
        }

        feedbackRepository.save(Feedback.builder()
                .memberId(memberId)
                .feedbackContent(content)
                .feedbackDate(new Date())
                .build());
    }
}
