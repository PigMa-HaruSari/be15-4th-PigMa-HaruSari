package com.pigma.harusari.diary.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @Column(name = "diary_title", length = 100, nullable = false)
    private String diaryTitle;

    @Column(name = "diary_content", columnDefinition = "TEXT", nullable = false)
    private String diaryContent;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    public Diary(Long memberId, String diaryTitle, String diaryContent) {
        this.diaryTitle = diaryTitle;
        this.diaryContent = diaryContent;
        this.memberId = memberId;
        this.createdAt = LocalDateTime.now();
    }

    // 수정은 당일만 가능하도록 외부에서 검증 후 호출
    public void update(String diaryTitle, String diaryContent) {
        this.diaryTitle = diaryTitle;
        this.diaryContent = diaryContent;
        this.modifiedAt = LocalDateTime.now();
    }
}
