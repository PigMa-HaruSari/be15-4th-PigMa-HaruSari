package com.pigma.harusari.feedback.command.service;

import com.pigma.harusari.diary.command.domain.aggregate.Diary;
import com.pigma.harusari.feedback.command.entity.Feedback;
import com.pigma.harusari.feedback.command.repository.FeedbackRepository;
import com.pigma.harusari.feedback.util.FeedbackPromptBuilder;
import com.pigma.harusari.feedback.util.GeminiClient;
import com.pigma.harusari.task.schedule.command.entity.Schedule;
import com.pigma.harusari.task.schedule.query.mapper.TaskScheduleQueryMapper;
import com.pigma.harusari.diary.query.mapper.DiaryQueryMapper;
import com.pigma.harusari.user.query.mapper.UserQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[피드백 - FeedbackCommandService] FeedbackCommandServiceImplTest 테스트")
class FeedbackCommandServiceImplTest {

    @Mock
    private DiaryQueryMapper diaryQueryMapper;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private FeedbackPromptBuilder promptBuilder;

    @Mock
    private GeminiClient geminiClient;

    @Mock
    private UserQueryMapper userQueryMapper;

    @Mock
    private TaskScheduleQueryMapper scheduleQueryMapper;

    @InjectMocks
    private FeedbackCommandServiceImpl feedbackCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("feedback 생성")
    void generateMonthlyFeedbackForAllUsers_shouldSaveFeedbackOnlyForEligibleUsers() {
        // given
        Long user1 = 1L;
        Long user2 = 2L;

        when(userQueryMapper.getAllActiveUserIds()).thenReturn(List.of(user1, user2));

        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate start = lastMonth.atDay(1);
        LocalDate end = lastMonth.atEndOfMonth();

        // user1: diaries 5개, schedules 5개 (조건 만족)
        List<Diary> diariesUser1 = List.of(
                Diary.builder().memberId(user1).diaryTitle("title1").diaryContent("content1").build(),
                Diary.builder().memberId(user1).diaryTitle("title2").diaryContent("content2").build(),
                Diary.builder().memberId(user1).diaryTitle("title3").diaryContent("content3").build(),
                Diary.builder().memberId(user1).diaryTitle("title4").diaryContent("content4").build(),
                Diary.builder().memberId(user1).diaryTitle("title5").diaryContent("content5").build()
        );
        List<Schedule> schedulesUser1 = List.of(
                Schedule.builder().scheduleContent("schedule1").completionStatus(true).build(),
                Schedule.builder().scheduleContent("schedule2").completionStatus(true).build(),
                Schedule.builder().scheduleContent("schedule3").completionStatus(false).build(),
                Schedule.builder().scheduleContent("schedule4").completionStatus(true).build(),
                Schedule.builder().scheduleContent("schedule5").completionStatus(false).build()
        );

        // user2: diaries 4개, schedules 3개 (조건 미충족)
        List<Diary> diariesUser2 = List.of(
                Diary.builder().memberId(user2).diaryTitle("title1").diaryContent("content1").build(),
                Diary.builder().memberId(user2).diaryTitle("title2").diaryContent("content2").build(),
                Diary.builder().memberId(user2).diaryTitle("title3").diaryContent("content3").build(),
                Diary.builder().memberId(user2).diaryTitle("title4").diaryContent("content4").build()
        );
        List<Schedule> schedulesUser2 = List.of(
                Schedule.builder().scheduleContent("schedule1").completionStatus(true).build(),
                Schedule.builder().scheduleContent("schedule2").completionStatus(false).build(),
                Schedule.builder().scheduleContent("schedule3").completionStatus(false).build()
        );

        when(diaryQueryMapper.getLastMonthDiaries(user1, start, end)).thenReturn(diariesUser1);
        when(scheduleQueryMapper.getLastMonthSchedules(user1, start, end)).thenReturn(schedulesUser1);
        when(scheduleQueryMapper.countCompletedSchedules(user1, start, end)).thenReturn(3);

        when(diaryQueryMapper.getLastMonthDiaries(user2, start, end)).thenReturn(diariesUser2);
        when(scheduleQueryMapper.getLastMonthSchedules(user2, start, end)).thenReturn(schedulesUser2);
        when(scheduleQueryMapper.countCompletedSchedules(user2, start, end)).thenReturn(1);

        when(promptBuilder.buildPrompt(anyList(), anyList(), anyDouble())).thenReturn("mock-prompt");
        when(geminiClient.generateFeedback("mock-prompt")).thenReturn("mock-feedback-content");

        // when
        feedbackCommandService.generateMonthlyFeedbackForAllUsers();

        // then
        ArgumentCaptor<Feedback> feedbackCaptor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository, times(1)).save(feedbackCaptor.capture()); // user1만 저장됨

        Feedback savedFeedback = feedbackCaptor.getValue();
        assertEquals(user1, savedFeedback.getMemberId());
        assertEquals("mock-feedback-content", savedFeedback.getFeedbackContent());
        assertNotNull(savedFeedback.getFeedbackDate());

        // user2는 피드백 생성 조건 미충족이므로 저장 안함
        verify(feedbackRepository, times(1)).save(any());
    }
}
