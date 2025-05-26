package com.pigma.harusari.task.automationSchedule.query.service;

import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.task.automationSchedule.command.entity.AutomationSchedule;
import com.pigma.harusari.task.automationSchedule.command.entity.RepeatType;
import com.pigma.harusari.task.automationSchedule.query.dto.response.AutomationScheduleDto;
import com.pigma.harusari.task.automationSchedule.query.mapper.AutomationScheduleMapper;
import com.pigma.harusari.task.exception.ScheduleNotFoundException;
import com.pigma.harusari.task.exception.TaskErrorCode;
import com.pigma.harusari.task.schedule.command.entity.Schedule;
import com.pigma.harusari.task.schedule.command.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[자동화 일정] AutomationScheduleQueryServiceImpl 테스트")
class AutomationScheduleQueryServiceImplTest {

    @InjectMocks
    private AutomationScheduleQueryServiceImpl automationScheduleQueryService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private AutomationScheduleMapper automationScheduleMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("가장 가까운 자동화 일정 조회 성공")
    void testGetNearestAutomationScheduleSuccess() {
        // given
        Long automationScheduleId = 1L;
        Long memberId = 10L;
        LocalDate now = LocalDate.now();

        AutomationSchedule automationSchedule = AutomationSchedule.builder()
                .categoryId(2L)
                .automationScheduleContent("테스트 내용")
                .repeatType(RepeatType.MONTHLY)
                .categoryId(2L)
                .build();

        try {
            java.lang.reflect.Field idField = AutomationSchedule.class.getDeclaredField("automationScheduleId");
            idField.setAccessible(true);
            idField.set(automationSchedule, automationScheduleId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("리플렉션으로 필드 세팅 실패: " + e.getMessage());
        }

        Category category = Category.builder()
                .categoryName("운동")
                .build();

        Schedule mockSchedule = Schedule.builder()
                .scheduleDate(now.plusDays(3))
                .automationSchedule(automationSchedule)
                .category(category)
                .build();

        when(scheduleRepository.findFirstByAutomationSchedule_AutomationScheduleIdAndScheduleDateGreaterThanEqualOrderByScheduleDateAsc(
                eq(automationScheduleId), any(LocalDate.class))
        ).thenReturn(Optional.of(mockSchedule));

        // when
        AutomationScheduleDto result = automationScheduleQueryService.getNearestAutomationSchedule(automationScheduleId, memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAutomationScheduleId()).isEqualTo(automationScheduleId);
        assertThat(result.getAutomationScheduleContent()).isEqualTo("테스트 내용");
        assertThat(result.getRepeatType()).isEqualTo("MONTHLY");
        assertThat(result.getCategoryId()).isEqualTo(2L);
        assertThat(result.getCategoryName()).isEqualTo("운동");
        verify(scheduleRepository).findFirstByAutomationSchedule_AutomationScheduleIdAndScheduleDateGreaterThanEqualOrderByScheduleDateAsc(
                automationScheduleId, LocalDate.now());
    }

    @Test
    @DisplayName("가장 가까운 일정 없을 때 예외 발생")
    void testGetNearestAutomationScheduleNotFound() {
        // given
        Long automationScheduleId = 1L;
        Long memberId = 10L;

        when(scheduleRepository.findFirstByAutomationSchedule_AutomationScheduleIdAndScheduleDateGreaterThanEqualOrderByScheduleDateAsc(
                anyLong(), any(LocalDate.class))
        ).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                automationScheduleQueryService.getNearestAutomationSchedule(automationScheduleId, memberId)
        ).isInstanceOf(ScheduleNotFoundException.class).hasMessageContaining(TaskErrorCode.SCHEDULE_NOT_FOUND.getMessage());
    }
}
