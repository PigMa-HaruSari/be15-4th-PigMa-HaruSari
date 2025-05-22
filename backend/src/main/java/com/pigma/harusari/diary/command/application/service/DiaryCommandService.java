package com.pigma.harusari.diary.command.application.service;


import com.pigma.harusari.diary.command.application.dto.request.DiaryCreateRequest;
import com.pigma.harusari.diary.command.application.dto.request.DiaryUpdateRequest;

public interface DiaryCommandService {

    Long createDiary(Long memberId, DiaryCreateRequest request);

    void updateDiary(Long diaryId, Long memberId, DiaryUpdateRequest request);

    void deleteDiary(Long diaryId, Long memberId);
}
