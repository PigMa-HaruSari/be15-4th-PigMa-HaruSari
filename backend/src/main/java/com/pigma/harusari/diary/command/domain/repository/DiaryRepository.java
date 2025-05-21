package com.pigma.harusari.diary.command.domain.repository;

import com.pigma.harusari.diary.command.domain.aggregate.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // 회고 작성 여부 체크 (하루 1개 제한)
    boolean existsByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);

    // 수정/삭제 시 본인 여부 확인용
    Optional<Diary> findByDiaryIdAndMemberId(Long diaryId, Long memberId);
}
