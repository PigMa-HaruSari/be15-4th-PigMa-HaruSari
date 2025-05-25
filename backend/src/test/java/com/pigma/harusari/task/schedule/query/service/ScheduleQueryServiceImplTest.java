package com.pigma.harusari.task.schedule.query.service;

import com.pigma.harusari.task.schedule.query.dto.response.ScheduleDto;
import com.pigma.harusari.task.schedule.query.dto.response.ScheduleListResponse;
import com.pigma.harusari.task.schedule.query.mapper.ScheduleMapper;
import com.pigma.harusari.task.schedule.query.service.ScheduleQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("[자동화 일정] ScheduleQueryServiceImpl 테스트")
class ScheduleQueryServiceImplTest {

    @InjectMocks
    private ScheduleQueryServiceImpl scheduleQueryService;

    @Mock
    private ScheduleMapper scheduleMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("일정 목록 조회 - 정상 케이스")
    void testGetScheduleListSuccess() {
        // given
        Long categoryId = 1L;
        LocalDate scheduleDate = LocalDate.of(2025, 6, 1);
        Long memberId = 10L;

        List<ScheduleDto> mockList = List.of(
                ScheduleDto.builder()
                        .scheduleId(100L)
                        .categoryId(1L)
                        .scheduleContent("테스트 일정")
                        .scheduleDate(scheduleDate)
                        .build()
        );

        when(scheduleMapper.selectScheduleByMemberId(categoryId, scheduleDate, memberId))
                .thenReturn(mockList);

        // when
        ScheduleListResponse response = scheduleQueryService.getScheduleList(categoryId, scheduleDate, memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getSchedule()).hasSize(1);
        assertThat(response.getSchedule().get(0).getScheduleContent()).isEqualTo("테스트 일정");
    }

    @Test
    @DisplayName("일정 목록 조회 - 빈 목록 반환")
    void testGetScheduleListEmpty() {
        // given
        Long categoryId = 1L;
        LocalDate scheduleDate = LocalDate.of(2025, 6, 1);
        Long memberId = 10L;

        when(scheduleMapper.selectScheduleByMemberId(categoryId, scheduleDate, memberId))
                .thenReturn(Collections.emptyList());

        // when
        ScheduleListResponse response = scheduleQueryService.getScheduleList(categoryId, scheduleDate, memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getSchedule()).isEmpty();
    }

    @Test
    @DisplayName("일정 목록 조회 - 파라미터 null 허용")
    void testGetScheduleListWithNullParams() {
        // given
        when(scheduleMapper.selectScheduleByMemberId(null, null, 10L))
                .thenReturn(Collections.emptyList());

        // when
        ScheduleListResponse response = scheduleQueryService.getScheduleList(null, null, 10L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getSchedule()).isEmpty();
    }
}
