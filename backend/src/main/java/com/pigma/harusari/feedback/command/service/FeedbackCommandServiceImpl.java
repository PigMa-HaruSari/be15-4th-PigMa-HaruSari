package com.pigma.harusari.feedback.command.service;

import com.pigma.harusari.diary.query.mapper.DiaryQueryMapper;
import com.pigma.harusari.feedback.command.entity.Feedback;
import com.pigma.harusari.feedback.command.repository.FeedbackRepository;
import com.pigma.harusari.feedback.util.FeedbackPromptBuilder;
import com.pigma.harusari.feedback.util.GeminiClient;
import com.pigma.harusari.task.schedule.query.Mapper.TaskScheduleQueryMapper;
import com.pigma.harusari.user.query.mapper.UserQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void generateMonthlyFeedbackForAllUsers() {
        List<Long> memberIds = userQueryMapper.getAllActiveUserIds();

        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate start = lastMonth.atDay(1);
        LocalDate end = lastMonth.atEndOfMonth();

        for (Long memberId : memberIds) {
            var diaries = diaryQueryMapper.getLastMonthDiaries(memberId, start, end);
            var schedules = scheduleQueryMapper.getLastMonthSchedules(memberId, start, end);

            int completed = scheduleQueryMapper.countCompletedSchedules(memberId, start, end);
            double achievementRate = schedules.isEmpty() ? 0 : (double) completed / schedules.size();

            String prompt = promptBuilder.buildPrompt(diaries, schedules, achievementRate);
            String content = geminiClient.generateFeedback(prompt);

            feedbackRepository.save(Feedback.builder()
                    .memberId(memberId)
                    .feedbackContent(content)
                    .feedbackDate(new Date())
                    .build());
        }
    }

    @Override
    public void generateMonthlyFeedbackForMember(Long memberId) {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate start = lastMonth.atDay(1);
        LocalDate end = lastMonth.atEndOfMonth();

        var diaries = diaryQueryMapper.getLastMonthDiaries(memberId, start, end);
        var schedules = scheduleQueryMapper.getLastMonthSchedules(memberId, start, end);

        int completed = scheduleQueryMapper.countCompletedSchedules(memberId, start, end);
        double achievementRate = schedules.isEmpty() ? 0 : (double) completed / schedules.size();

        String prompt = promptBuilder.buildPrompt(diaries, schedules, achievementRate);
        String content = geminiClient.generateFeedback(prompt);

        feedbackRepository.save(Feedback.builder()
                .memberId(memberId)
                .feedbackContent(content)
                .feedbackDate(new Date())
                .build());
    }
}
