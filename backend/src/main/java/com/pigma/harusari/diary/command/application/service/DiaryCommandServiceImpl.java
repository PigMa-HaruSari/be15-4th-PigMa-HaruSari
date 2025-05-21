package com.pigma.harusari.diary.command.application.service;


import com.pigma.harusari.diary.command.application.dto.request.DiaryCreateRequest;
import com.pigma.harusari.diary.command.application.dto.request.DiaryUpdateRequest;
import com.pigma.harusari.diary.command.domain.aggregate.Diary;
import com.pigma.harusari.diary.command.domain.repository.DiaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryCommandServiceImpl implements DiaryCommandService {

    private final DiaryRepository diaryRepository;

    @Override
    @Transactional
    public Long createDiary(DiaryCreateRequest request) {
        // 하루에 한 번만 작성 가능
        boolean alreadyExists = diaryRepository.existsByMemberIdAndCreatedAtBetween(
                request.getMemberId(),
                LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(23, 59, 59)
        );

        if (alreadyExists) {
            throw new IllegalStateException("오늘은 이미 회고를 작성하셨습니다.");
        }

        Diary diary = Diary.builder()
                .diaryTitle(request.getDiaryTitle())
                .diaryContent(request.getDiaryContent())
                .memberId(request.getMemberId())
                .build();

        return diaryRepository.save(diary).getDiaryId();
    }

    @Override
    @Transactional
    public void updateDiary(Long diaryId, Long memberId, DiaryUpdateRequest request) {
        Diary diary = diaryRepository.findByDiaryIdAndMemberId(diaryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("회고를 찾을 수 없습니다."));

        // 작성 당일만 수정 가능
        if (!diary.getCreatedAt().toLocalDate().isEqual(LocalDate.now())) {
            throw new IllegalStateException("작성 당일에만 수정할 수 있습니다.");
        }

        diary.update(request.getDiaryTitle(), request.getDiaryContent());
    }

    @Override
    @Transactional
    public void deleteDiary(Long diaryId, Long memberId) {
        Diary diary = diaryRepository.findByDiaryIdAndMemberId(diaryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("회고를 찾을 수 없습니다."));

        if (!diary.getCreatedAt().toLocalDate().isEqual(LocalDate.now())) {
            throw new IllegalStateException("작성 당일에만 삭제할 수 있습니다.");
        }

        diaryRepository.delete(diary);
    }
}
