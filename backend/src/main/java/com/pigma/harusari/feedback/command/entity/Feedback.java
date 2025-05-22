package com.pigma.harusari.feedback.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "feedback")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "feedback_content", columnDefinition = "TEXT")
    private String feedbackContent;

    @Column(name = "feedback_date")
    private Date feedbackDate;

    @Builder
    public Feedback(Long memberId, String feedbackContent, Date feedbackDate) {
        this.memberId = memberId;
        this.feedbackContent = feedbackContent;
        this.feedbackDate = feedbackDate;
    }
}
