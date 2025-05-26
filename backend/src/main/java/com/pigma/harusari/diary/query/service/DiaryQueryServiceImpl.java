package com.pigma.harusari.diary.query.service;

import com.pigma.harusari.diary.query.dto.DiaryResponseDto;
import com.pigma.harusari.diary.query.mapper.DiaryQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryQueryServiceImpl implements DiaryQueryService {

    private final DiaryQueryMapper diaryQueryMapper;

    @Override
    public DiaryResponseDto getDiaryByDate(Long memberId, LocalDate date) {
        return diaryQueryMapper.findDiaryByMemberIdAndDate(memberId, date);
    }
}
