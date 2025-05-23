package com.pigma.harusari.diary.query.mapper;

import com.pigma.harusari.diary.command.domain.aggregate.Diary;
import com.pigma.harusari.diary.query.dto.DiaryResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DiaryQueryMapper {
    List<Diary> getLastMonthDiaries(@Param("memberId") Long memberId,
                                    @Param("start") LocalDate start,
                                    @Param("end") LocalDate end);


    DiaryResponseDto findDiaryByMemberIdAndDate(@Param("memberId") Long memberId, @Param("date") LocalDate date);
}
