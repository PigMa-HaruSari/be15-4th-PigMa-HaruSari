package com.pigma.harusari.feedback.util;

import com.pigma.harusari.diary.command.domain.aggregate.Diary;
import com.pigma.harusari.task.schedule.command.entity.Schedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("[피드백 - util] FeedbackPromptBuilder 테스트")
class FeedbackPromptBuilderTest {

    private final FeedbackPromptBuilder builder = new FeedbackPromptBuilder();

    @Test
    @DisplayName("buildPrompt() - 회고, 일정, 달성률을 포함한 피드백 프롬프트 생성")
    void buildPrompt_success() {
        // given
        Diary diary1 = mock(Diary.class);
        when(diary1.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 5, 1, 10, 0));
        when(diary1.getDiaryContent()).thenReturn("첫 번째 회고 내용");

        Diary diary2 = mock(Diary.class);
        when(diary2.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 5, 15, 12, 0));
        when(diary2.getDiaryContent()).thenReturn("두 번째 회고 내용");

        List<Diary> diaries = List.of(diary1, diary2);

        Schedule schedule1 = mock(Schedule.class);
        when(schedule1.getCompletionStatus()).thenReturn(true);

        Schedule schedule2 = mock(Schedule.class);
        when(schedule2.getCompletionStatus()).thenReturn(false);

        Schedule schedule3 = mock(Schedule.class);
        when(schedule3.getCompletionStatus()).thenReturn(true);

        List<Schedule> schedules = List.of(schedule1, schedule2, schedule3);

        double rate = 0.75;

        // when
        String prompt = builder.buildPrompt(diaries, schedules, rate);

        // then
        assertThat(prompt).contains("- 2025-05-01: \"첫 번째 회고 내용\"");
        assertThat(prompt).contains("- 2025-05-15: \"두 번째 회고 내용\"");
        assertThat(prompt).contains("총 3개 일정 중 2개 완료");
        assertThat(prompt).contains("75.0%");
        assertThat(prompt).contains("당신은 일정/회고 분석 전문가입니다.");
        assertThat(prompt).contains("잘한 점과 개선할 점을 친절한 피드백 형식으로 작성해주세요");
    }
}
