package com.pigma.harusari.task.schedule.command.service;

import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import com.pigma.harusari.task.automationSchedule.command.entity.AutomationSchedule;
import com.pigma.harusari.task.automationSchedule.command.repository.AutomationScheduleRepository;
import com.pigma.harusari.task.exception.*;
import com.pigma.harusari.task.infrastructure.repository.JpaScheduleRepository;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleCreateRequest;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleUpdateRequest;
import com.pigma.harusari.task.schedule.command.dto.response.ScheduleCommandResponse;
import com.pigma.harusari.task.schedule.command.entity.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[자동화 일정] ScheduleCommandServiceImpl 테스트")
class ScheduleCommandServiceImplTest {

    @InjectMocks
    private ScheduleCommandServiceImpl scheduleCommandService;

    @Mock
    private JpaScheduleRepository scheduleRepository;
    @Mock
    private CategoryCommandRepository categoryCommandRepository;
    @Mock
    private AutomationScheduleRepository automationScheduleRepository;

    @Mock
    private ModelMapper modelMapper;

    Category category;
    Schedule schedule;
    AutomationSchedule automationSchedule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = Category.builder()
                .categoryId(1L)
                .memberId(10L)
                .categoryName("운동")
                .build();

        automationSchedule = AutomationSchedule.builder()
                .categoryId(1L)
                .automationScheduleContent("자동화 내용")
                .build();

        // 리플렉션으로 ID 세팅 (테스트 환경에서만)
        try {
            java.lang.reflect.Field idField = AutomationSchedule.class.getDeclaredField("automationScheduleId");
            idField.setAccessible(true);
            idField.set(automationSchedule, 100L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    schedule = Schedule.builder()
            .category(category)
                .automationSchedule(automationSchedule)
                .scheduleContent("테스트 일정")
                .scheduleDate(LocalDate.now())
            .completionStatus(false)
                .build();
        try {
        java.lang.reflect.Field idField = Schedule.class.getDeclaredField("scheduleId");
        idField.setAccessible(true);
        idField.set(schedule, 200L);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

    @Test
    @DisplayName("일정 생성 성공")
    void testCreateScheduleSuccess() {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .categoryId(1L)
                .scheduleContent("테스트 일정")
                .scheduleDate(LocalDate.now().plusDays(1))
                .automationScheduleId(100L)
                .build();

        Schedule newSchedule = Schedule.builder()
                .category(category)
                .automationSchedule(automationSchedule)
                .scheduleContent("테스트 일정")
                .scheduleDate(LocalDate.now().plusDays(1))
                .build();

        // ModelMapper가 request를 Schedule로 변환하도록 Mock 세팅
        when(modelMapper.map(any(ScheduleCreateRequest.class), eq(Schedule.class))).thenReturn(newSchedule);

        when(categoryCommandRepository.findById(1L)).thenReturn(Optional.of(category));
        when(automationScheduleRepository.findById(100L)).thenReturn(Optional.of(automationSchedule));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> {
            Schedule s = invocation.getArgument(0);
            // 리플렉션으로 ID 세팅
            try {
                java.lang.reflect.Field idField = Schedule.class.getDeclaredField("scheduleId");
                idField.setAccessible(true);
                idField.set(s, 999L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return s;
        });

        // when & then (예외가 발생하지 않으면 성공)
        assertThatCode(() -> scheduleCommandService.createSchedule(request)).doesNotThrowAnyException();

        verify(categoryCommandRepository).findById(1L);
        verify(automationScheduleRepository).findById(100L);
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    @DisplayName("카테고리 미존재 시 예외 발생")
    void testCreateScheduleCategoryNotFound() {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .categoryId(2L)
                .scheduleContent("테스트 일정")
                .scheduleDate(LocalDate.now().plusDays(1))
                .build();

        when(categoryCommandRepository.findById(2L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.createSchedule(request))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("카테고리를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("자동화 일정 미존재 시 예외 발생")
    void testCreateScheduleAutomationScheduleNotFound() {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .categoryId(1L)
                .scheduleContent("테스트 일정")
                .scheduleDate(LocalDate.now().plusDays(1))
                .automationScheduleId(200L)
                .build();

        when(categoryCommandRepository.findById(1L)).thenReturn(Optional.of(category));
        when(automationScheduleRepository.findById(200L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.createSchedule(request))
                .isInstanceOf(AutomationScheduleNotFoundException.class)
                .hasMessageContaining("자동화 일정을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("과거 날짜로 일정 생성 시 예외 발생")
    void testCreateSchedulePastDate() {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .categoryId(1L)
                .scheduleContent("테스트 일정")
                .scheduleDate(LocalDate.now().minusDays(1))
                .build();

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.createSchedule(request))
                .isInstanceOf(InvalidScheduleDateException.class)
                .hasMessageContaining("과거 날짜로는 일정을 등록하실 수 없습니다");
    }

    @Test
    @DisplayName("일정 날짜 미입력 시 예외 발생")
    void testCreateScheduleMissingDate() {
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .categoryId(1L)
                .scheduleContent("테스트 일정")
                .scheduleDate(null)
                .build();

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.createSchedule(request))
                .isInstanceOf(TaskInvalidDateFormatException.class)
                .hasMessageContaining("일정 날짜가 입력되지 않았습니다");
    }


    @Test
    @DisplayName("일정 수정 성공")
    void testUpdateScheduleSuccess() {
        // given
        Long scheduleId = 200L;
        Long memberId = 10L;

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
                .categoryId(1L)
                .scheduleContent("수정된 일정")
                .scheduleDate(LocalDate.now().plusDays(2))
                .build();

        // when
        scheduleCommandService.updateSchedule(scheduleId, request, memberId);

        // then
        assertThat(schedule.getScheduleContent()).isEqualTo("수정된 일정");
        assertThat(schedule.getScheduleDate()).isEqualTo(LocalDate.now().plusDays(2));
        verify(scheduleRepository).save(schedule);
    }

    @Test
    @DisplayName("일정 수정 - 권한 없는 사용자")
    void testUpdateScheduleUnauthorized() {
        // given
        Long scheduleId = 200L;
        Long memberId = 999L; // category.memberId와 다름

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.of(schedule));

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
                .categoryId(1L)
                .scheduleContent("수정된 일정")
                .scheduleDate(LocalDate.now().plusDays(2))
                .build();

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.updateSchedule(scheduleId, request, memberId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("수정 권한이 없습니다.");
    }

    @Test
    @DisplayName("일정 수정 - 완료된 일정")
    void testUpdateScheduleAlreadyCompleted() {
        // given
        schedule.setCompletionStatus(true);
        Long scheduleId = 200L;
        Long memberId = 10L;

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.of(schedule));

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
                .categoryId(1L)
                .scheduleContent("수정된 일정")
                .scheduleDate(LocalDate.now().plusDays(2))
                .build();

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.updateSchedule(scheduleId, request, memberId))
                .isInstanceOf(ScheduleAlreadyCompletedException.class);
    }

    @Test
    @DisplayName("일정 수정 - 과거 일정")
    void testUpdateSchedulePastDate() {
        // given
        schedule.setScheduleDate(LocalDate.now().minusDays(1));
        Long scheduleId = 200L;
        Long memberId = 10L;

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.of(schedule));

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
                .categoryId(1L)
                .scheduleContent("수정된 일정")
                .scheduleDate(LocalDate.now().plusDays(2))
                .build();

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.updateSchedule(scheduleId, request, memberId))
                .isInstanceOf(InvalidScheduleDateException.class);
    }

    @Test
    @DisplayName("일정 수정 - 카테고리 변경 시 권한 예외")
    void testUpdateScheduleChangeCategoryUnauthorized() {
        // given
        Long scheduleId = 200L;
        Long memberId = 10L;
        Category newCategory = Category.builder()
                .categoryId(2L)
                .memberId(999L)
                .categoryName("다른 사람 카테고리")
                .build();

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.of(schedule));
        when(categoryCommandRepository.findById(2L)).thenReturn(Optional.of(newCategory));

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
                .categoryId(2L)
                .scheduleContent("수정된 일정")
                .scheduleDate(LocalDate.now().plusDays(2))
                .build();

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.updateSchedule(scheduleId, request, memberId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("해당 카테고리에 대한 권한이 없습니다.");
    }



    @Test
    @DisplayName("일정 삭제 - 일정 미존재")
    void testDeleteScheduleNotFound() {
        // given
        Long scheduleId = 999L;
        Long memberId = 10L;
        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.deleteSchedule(scheduleId, memberId))
                .isInstanceOf(ScheduleNotFoundException.class);
    }

    // --- updateCompletionStatus 테스트 ---

    @Test
    @DisplayName("일정 완료 상태 수정 성공(오늘 일정)")
    void testUpdateCompletionStatusSuccess() {
        // given
        Long scheduleId = 200L;
        Long memberId = 10L;
        Boolean completionStatus = true;

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        // when
        ScheduleCommandResponse response = scheduleCommandService.updateCompletionStatus(scheduleId, completionStatus, memberId);

        // then
        assertThat(response.getScheduleId()).isEqualTo(scheduleId);
        assertThat(response.getCompletionStatus()).isTrue();
        verify(scheduleRepository).save(schedule);
    }

    @Test
    @DisplayName("일정 완료 상태 수정 - 권한 없는 사용자")
    void testUpdateCompletionStatusUnauthorized() {
        // given
        Long scheduleId = 200L;
        Long memberId = 999L;

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.of(schedule));

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.updateCompletionStatus(scheduleId, true, memberId))
                .isInstanceOf(UnauthorizedModificationException.class);
    }

    @Test
    @DisplayName("일정 완료 상태 수정 - 오늘이 아닌 일정 완료 시 예외")
    void testUpdateCompletionStatusNotToday() {
        // given
        schedule.setScheduleDate(LocalDate.now().plusDays(1)); // 내일 일정
        Long scheduleId = 200L;
        Long memberId = 10L;

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.of(schedule));

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.updateCompletionStatus(scheduleId, true, memberId))
                .isInstanceOf(InvalidScheduleDateException.class)
                .hasMessageContaining("오늘 일정만 완료 처리할 수 있습니다");
    }

    @Test
    @DisplayName("일정 완료 상태 수정 - 일정 미존재")
    void testUpdateCompletionStatusNotFound() {
        // given
        Long scheduleId = 999L;
        Long memberId = 10L;

        when(scheduleRepository.findByScheduleId(scheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleCommandService.updateCompletionStatus(scheduleId, true, memberId))
                .isInstanceOf(ScheduleNotFoundException.class);
    }

}
