package com.pigma.harusari.feedback.util;

import com.pigma.harusari.diary.command.domain.aggregate.Diary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedbackPromptBuilder {
    public String buildPrompt(List<Diary> diaries, List<Schedule> schedules, double rate) {
        String diaryText = diaries.stream()
                .map(d -> String.format("- %s: \"%s\"", d.getCreatedAt().toLocalDate(), d.getDiaryContent()))
                .collect(Collectors.joining("\n"));

        String scheduleSummary = String.format("총 %d개 일정 중 %d개 완료",
                schedules.size(),
                schedules.stream().filter(s -> s.getCompletionStatus() == 1).count());

        return String.format("""
당신은 일정/회고 분석 전문가입니다.
다음은 한 사용자의 지난달 회고와 일정 내용, 달성률입니다.

[회고]\n%s

[일정]\n%s

[달성률]\n%.1f%%

위 데이터를 기반으로 잘한 점과 개선할 점을 친절한 피드백 형식으로 작성해주세요.
""", diaryText, scheduleSummary, rate * 100);
    }
}
