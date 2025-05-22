package com.pigma.harusari.feedback.command.repository;

import com.pigma.harusari.feedback.command.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
