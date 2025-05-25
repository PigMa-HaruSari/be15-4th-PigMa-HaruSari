package com.pigma.harusari.task.automationSchedule.command.service;

import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import com.pigma.harusari.task.automationSchedule.command.controller.AutomationScheduleController;
import com.pigma.harusari.task.automationSchedule.command.dto.request.AutomationScheduleCreateRequest;
import com.pigma.harusari.task.automationSchedule.command.entity.AutomationSchedule;
import com.pigma.harusari.task.automationSchedule.command.entity.RepeatType;
import com.pigma.harusari.task.automationSchedule.command.repository.AutomationScheduleRepository;
import com.pigma.harusari.task.exception.CategoryNotFoundException;
import com.pigma.harusari.task.exception.ScheduleNotFoundException;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleCreateRequest;
import com.pigma.harusari.task.schedule.command.repository.ScheduleRepository;
import com.pigma.harusari.task.schedule.command.service.ScheduleCommandService;
import com.pigma.harusari.task.exception.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[자동화 일정] AutomationScheduleServiceImpl 테스트")
class AutomationScheduleServiceImplTest {

    @InjectMocks
    private AutomationScheduleServiceImpl automationScheduleService;

    @Mock
    private AutomationScheduleRepository automationScheduleRepository;

    @Mock
    private ScheduleCommandService scheduleCommandService;

    @Mock
    private CategoryCommandRepository categoryCommandRepository;

    @Captor
    private ArgumentCaptor<AutomationSchedule> automationScheduleCaptor;

    @Mock
    private ScheduleRepository scheduleRepository;

    AutomationScheduleCreateRequest createRequest;
    Category category;
    AutomationScheduleCreateRequest updateRequest;
    AutomationSchedule schedule;
    AutomationSchedule automationSchedule;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createRequest = AutomationScheduleCreateRequest.builder()
                .categoryId(1L)
                .automationScheduleContent("자동화 내용")
                .endDate(LocalDate.of(2025, 6, 30))
                .repeatType("WEEKLY")
                .repeatWeekdays("MO,WE")
                .repeatMonthday(null)
                .build();

        category = Category.builder()
                .categoryId(1L)
                .memberId(10L)
                .categoryName("운동")
                .build();

        updateRequest = AutomationScheduleCreateRequest.builder()
                .categoryId(2L)
                .automationScheduleContent("수정된 내용")
                .endDate(LocalDate.of(2025, 7, 1))
                .repeatType("MONTHLY")
                .repeatWeekdays(null)
                .repeatMonthday(15)
                .build();

        schedule = AutomationSchedule.builder()
                .categoryId(1L)
                .automationScheduleContent("기존 내용")
                .endDate(LocalDate.of(2025, 6, 30))
                .repeatType(RepeatType.WEEKLY)
                .repeatWeekdays("MO,WE")
                .repeatMonthday(null)
                .build();

        // 리플렉션 등으로 ID 세팅 (테스트 환경에서)
        try {
            java.lang.reflect.Field idField = AutomationSchedule.class.getDeclaredField("automationScheduleId");
            idField.setAccessible(true);
            idField.set(schedule, 100L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        category = Category.builder()
                .categoryId(2L)
                .memberId(10L)
                .categoryName("수정 카테고리")
                .build();
        automationSchedule = AutomationSchedule.builder()
                .categoryId(1L)
                .automationScheduleContent("자동화 내용")
                .endDate(LocalDate.of(2025, 6, 30))
                .repeatType(/* 적절한 RepeatType 값 */ null)
                .repeatWeekdays("MO,WE")
                .repeatMonthday(null)
                .build();

        // 리플렉션으로 ID 세팅
        try {
            Field idField = AutomationSchedule.class.getDeclaredField("automationScheduleId");
            idField.setAccessible(true);
            idField.set(automationSchedule, 100L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        category = Category.builder()
                .categoryId(1L)
                .memberId(10L)
                .categoryName("운동")
                .build();
    }


@Test
    @DisplayName("자동화 일정 생성 성공")
    void testCreateAutomationScheduleSuccess() {
        // given
        Long memberId = 10L;
        when(categoryCommandRepository.findById(1L)).thenReturn(Optional.of(category));
        when(automationScheduleRepository.save(any(AutomationSchedule.class)))
                .thenAnswer(invocation -> {
                    AutomationSchedule saved = invocation.getArgument(0);
                    Field idField = AutomationSchedule.class.getDeclaredField("automationScheduleId");
                    idField.setAccessible(true);
                    idField.set(saved, 1L);
                    return saved;
                });

        // when
        Long result = automationScheduleService.createAutomationSchedule(createRequest, memberId);

        // then
        verify(categoryCommandRepository).findById(1L);
        verify(automationScheduleRepository).save(any(AutomationSchedule.class));
        verify(scheduleCommandService, atLeastOnce()).createSchedule(any(ScheduleCreateRequest.class));
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("카테고리 미존재 시 예외 발생")
    void testCreateAutomationScheduleCategoryNotFound() {
        // given
        Long memberId = 10L;
        when(categoryCommandRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> automationScheduleService.createAutomationSchedule(createRequest, memberId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("카테고리를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("카테고리 소유자 불일치 시 예외 발생")
    void testCreateAutomationScheduleUnauthorized() {
        // given
        Long memberId = 999L; // category.memberId와 다름
        when(categoryCommandRepository.findById(1L)).thenReturn(Optional.of(category));

        // when & then
        assertThatThrownBy(() -> automationScheduleService.createAutomationSchedule(createRequest, memberId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Unauthorized to create schedule");
    }

    @Test
    @DisplayName("자동화 일정 수정 성공")
    void testUpdateAutomationScheduleSuccess() {
        // given
        Long scheduleId = 100L;
        Long memberId = 10L;

        when(automationScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(categoryCommandRepository.findById(updateRequest.getCategoryId())).thenReturn(Optional.of(category));

        // when
        automationScheduleService.updateAutomationSchedule(scheduleId, updateRequest, memberId);

        // then
        verify(automationScheduleRepository).findById(scheduleId);
        verify(categoryCommandRepository).findById(updateRequest.getCategoryId());
        verify(scheduleCommandService).deleteSchedulesAfter(scheduleId, LocalDate.now());
        verify(scheduleCommandService, atLeastOnce()).createSchedule(any()); // 실제 일정 재생성
        // 필드가 정상적으로 업데이트 되었는지 검증
        assertThat(schedule.getCategoryId()).isEqualTo(updateRequest.getCategoryId());
        assertThat(schedule.getAutomationScheduleContent()).isEqualTo(updateRequest.getAutomationScheduleContent());
        assertThat(schedule.getEndDate()).isEqualTo(updateRequest.getEndDate());
        assertThat(schedule.getRepeatType()).isEqualTo(RepeatType.valueOf(updateRequest.getRepeatType()));
        assertThat(schedule.getRepeatWeekdays()).isEqualTo(updateRequest.getRepeatWeekdays());
        assertThat(schedule.getRepeatMonthday()).isEqualTo(updateRequest.getRepeatMonthday());
    }

    @Test
    @DisplayName("스케줄 미존재 시 예외 발생")
    void testUpdateAutomationScheduleNotFound() {
        // given
        Long scheduleId = 100L;
        Long memberId = 10L;
        when(automationScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                automationScheduleService.updateAutomationSchedule(scheduleId, updateRequest, memberId)
        ).isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Schedule not found");
    }

    @Test
    @DisplayName("카테고리 미존재 시 예외 발생")
    void testUpdateAutomationScheduleCategoryNotFound() {
        // given
        Long scheduleId = 100L;
        Long memberId = 10L;
        when(automationScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(categoryCommandRepository.findById(updateRequest.getCategoryId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                automationScheduleService.updateAutomationSchedule(scheduleId, updateRequest, memberId)
        ).isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    @DisplayName("권한 없는 사용자가 수정 시 예외 발생")
    void testUpdateAutomationScheduleUnauthorized() {
        // given
        Long scheduleId = 100L;
        Long memberId = 999L; // category.memberId와 다름
        when(automationScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(categoryCommandRepository.findById(updateRequest.getCategoryId())).thenReturn(Optional.of(category));

        // when & then
        assertThatThrownBy(() ->
                automationScheduleService.updateAutomationSchedule(scheduleId, updateRequest, memberId)
        ).isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("not allowed");
    }

    @Test
    @DisplayName("오늘 이후 일정 삭제 성공")
    void testDeleteSchedulesAfterSuccess() {
        // given
        Long automationScheduleId = 100L;
        Long memberId = 10L;

        when(automationScheduleRepository.findById(automationScheduleId)).thenReturn(Optional.of(automationSchedule));
        when(categoryCommandRepository.findById(automationSchedule.getCategoryId())).thenReturn(Optional.of(category));

        // when
        automationScheduleService.deleteSchedulesAfter(automationScheduleId, memberId);

        // then
        verify(automationScheduleRepository).findById(automationScheduleId);
        verify(categoryCommandRepository).findById(automationSchedule.getCategoryId());
        verify(scheduleRepository).deleteByAutomationSchedule_AutomationScheduleIdAndScheduleDateAfter(eq(automationScheduleId), any(LocalDate.class));
    }

    @Test
    @DisplayName("자동화 일정 미존재 시 예외 발생")
    void testDeleteSchedulesAfterScheduleNotFound() {
        // given
        Long automationScheduleId = 100L;
        Long memberId = 10L;
        when(automationScheduleRepository.findById(automationScheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                automationScheduleService.deleteSchedulesAfter(automationScheduleId, memberId)
        ).isInstanceOf(ScheduleNotFoundException.class)
                .hasMessageContaining("일정을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("카테고리 미존재 시 예외 발생")
    void testDeleteSchedulesAfterCategoryNotFound() {
        // given
        Long automationScheduleId = 100L;
        Long memberId = 10L;
        when(automationScheduleRepository.findById(automationScheduleId)).thenReturn(Optional.of(automationSchedule));
        when(categoryCommandRepository.findById(automationSchedule.getCategoryId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                automationScheduleService.deleteSchedulesAfter(automationScheduleId, memberId)
        ).isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("카테고리를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("권한 없는 사용자가 삭제 시 예외 발생")
    void testDeleteSchedulesAfterUnauthorized() {
        // given
        Long automationScheduleId = 100L;
        Long memberId = 999L; // category.memberId와 다름
        when(automationScheduleRepository.findById(automationScheduleId)).thenReturn(Optional.of(automationSchedule));
        when(categoryCommandRepository.findById(automationSchedule.getCategoryId())).thenReturn(Optional.of(category));

        // when & then
        assertThatThrownBy(() ->
                automationScheduleService.deleteSchedulesAfter(automationScheduleId, memberId)
        ).isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("삭제할 권한이 없습니다");
    }


}

