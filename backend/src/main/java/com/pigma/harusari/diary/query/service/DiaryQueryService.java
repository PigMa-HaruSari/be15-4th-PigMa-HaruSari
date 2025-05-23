package com.pigma.harusari.diary.query.service;

import com.pigma.harusari.diary.query.dto.DiaryResponseDto;
import com.pigma.harusari.diary.query.mapper.DiaryQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryQueryService {

    private final DiaryQueryMapper diaryQueryMapper;

    public DiaryResponseDto getDiaryByDate(Long memberId, LocalDate date) {
        return diaryQueryMapper.findDiaryByMemberIdAndDate(memberId, date);
    }
}
