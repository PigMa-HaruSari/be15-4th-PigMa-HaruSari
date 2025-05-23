package com.pigma.harusari.diary.query.service;

import com.pigma.harusari.diary.query.dto.DiaryResponseDto;

import java.time.LocalDate;

public interface DiaryQueryService {

    DiaryResponseDto getDiaryByDate(Long memberId, LocalDate date);
}
