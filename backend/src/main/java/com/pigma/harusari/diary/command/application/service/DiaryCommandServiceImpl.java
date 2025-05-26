package com.pigma.harusari.diary.command.application.service;

import com.pigma.harusari.diary.command.application.dto.request.DiaryCreateRequest;
import com.pigma.harusari.diary.command.application.dto.request.DiaryUpdateRequest;
import com.pigma.harusari.diary.command.domain.aggregate.Diary;
import com.pigma.harusari.diary.command.domain.repository.DiaryRepository;
import com.pigma.harusari.diary.exception.DiaryErrorCode;
import com.pigma.harusari.diary.exception.DiaryException;
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
    public Long createDiary(Long memberId, DiaryCreateRequest request) {
        // 하루에 한 번만 작성 가능
        boolean alreadyExists = diaryRepository.existsByMemberIdAndCreatedAtBetween(
                request.getMemberId(),
                LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(23, 59, 59)
        );

        if (alreadyExists) {
            throw new DiaryException(DiaryErrorCode.DUPLICATE_DIARY);
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
                .orElseThrow(() -> new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND));

        // 작성 당일만 수정 가능
        if (!diary.getCreatedAt().toLocalDate().isEqual(LocalDate.now())) {
            throw new DiaryException(DiaryErrorCode.INVALID_MODIFICATION);
        }

        diary.update(request.getDiaryTitle(), request.getDiaryContent());
    }

    @Override
    @Transactional
    public void deleteDiary(Long diaryId, Long memberId) {
        Diary diary = diaryRepository.findByDiaryIdAndMemberId(diaryId, memberId)
                .orElseThrow(() -> new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND));

        // 작성 당일만 삭제 가능
        if (!diary.getCreatedAt().toLocalDate().isEqual(LocalDate.now())) {
            throw new DiaryException(DiaryErrorCode.INVALID_DELETION);
        }

        diaryRepository.delete(diary);
    }
}
