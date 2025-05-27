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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    private final JpaScheduleRepository scheduleRepository;
    private final CategoryCommandRepository categoryCommandRepository;
    private final AutomationScheduleRepository automationScheduleRepository;

    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void createSchedule(
            ScheduleCreateRequest scheduleCreateRequest
    ) {
        LocalDate scheduleDate = scheduleCreateRequest.getScheduleDate();

        if (scheduleDate == null) {
            throw new TaskInvalidDateFormatException(TaskErrorCode.MISSING_SCHEDULE_DATE);
        }

        // 과거 날짜 검증
        if (scheduleDate.isBefore(LocalDate.now())) {
            throw new InvalidScheduleDateException(TaskErrorCode.INVALID_SCHEDULE_DATE);
        }

        Category category = categoryCommandRepository.findById(scheduleCreateRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(TaskErrorCode.CATEGORY_NOT_FOUND));

        AutomationSchedule automationSchedule = null;
        Long automationScheduleId = scheduleCreateRequest.getAutomationScheduleId();

        if (automationScheduleId != null) {
            automationSchedule = automationScheduleRepository.findById(automationScheduleId)
                    .orElseThrow(() -> new AutomationScheduleNotFoundException(TaskErrorCode.AUTOMATION_SCHEDULE_NOT_FOUND));
        }

        Schedule newSchedule = modelMapper.map(scheduleCreateRequest, Schedule.class);
        newSchedule.setAutomationSchedule(automationSchedule);
        newSchedule.setCategory(category);

        Schedule schedule = scheduleRepository.save(newSchedule);

        ScheduleCommandResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .automationScheduleId(
                        schedule.getAutomationSchedule() != null ? schedule.getAutomationSchedule().getAutomationScheduleId() : null
                )
                .categoryId(schedule.getCategory().getCategoryId())
                .scheduleContent(schedule.getScheduleContent())
                .scheduleDate(schedule.getScheduleDate())
                .completionStatus(schedule.getCompletionStatus())
                .build();
    }

    // 자동화 일정 id와 기준일 이후의 일정 삭제
    @Transactional
    public void deleteSchedulesAfter(Long automationScheduleId, LocalDate baseDate) {
        scheduleRepository.deleteByAutomationSchedule_AutomationScheduleIdAndScheduleDateAfter(automationScheduleId, baseDate);
    }

    // 자동화 일정 id와 기준일 이후의 일정 일괄 변경
    @Transactional
    public void updateSchedulesAfterBaseDate(Long automationScheduleId, LocalDate baseDate, String newContent) {
        List<Schedule> schedules = scheduleRepository
                .findByAutomationSchedule_AutomationScheduleIdAndScheduleDateAfter(automationScheduleId, baseDate);

        for (Schedule schedule : schedules) {
            schedule.setScheduleContent(newContent);
            // 필요시 다른 필드도 변경
        }
        scheduleRepository.saveAll(schedules);
    }

    // 사용자 ID 검증 추가

    @Transactional
    public Long createSchedule(ScheduleCreateRequest request, Long memberId) {
        // 1. 카테고리 존재 및 소유자 검증
        Category category = categoryCommandRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(TaskErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to create schedule for this category");
        }

        // 2. 자동화 스케줄 존재 시 검증 및 조회
        AutomationSchedule automationSchedule = null;
        if (request.getAutomationScheduleId() != null) {
            automationSchedule = automationScheduleRepository.findById(request.getAutomationScheduleId())
                    .orElseThrow(() -> new AutomationScheduleNotFoundException(TaskErrorCode.AUTOMATION_SCHEDULE_NOT_FOUND));
        }

        // 3. Schedule 엔티티 생성
        Schedule schedule = Schedule.builder()
                .category(category)
                .automationSchedule(automationSchedule)
                .scheduleContent(request.getScheduleContent())
                .scheduleDate(request.getScheduleDate())
                .completionStatus(false)
                .build();

        // 4. 저장 및 PK 반환
        scheduleRepository.save(schedule);
        return schedule.getScheduleId();
    }


    @Transactional
    @Override
    public void updateSchedule(Long scheduleId, ScheduleUpdateRequest request, Long memberId) {
        // 1. 일정 조회
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(TaskErrorCode.SCHEDULE_NOT_FOUND));

        // 2. 소유자(memberId) 검증 - 가장 먼저!
        if (!schedule.getCategory().getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다.");
        }

        // 3. 완료된 일정은 수정 불가
        if (schedule.getCompletionStatus()) {
            throw new ScheduleAlreadyCompletedException(TaskErrorCode.SCHEDULE_ALREADY_COMPLETED);
        }

        // 4. 과거 일정은 수정 불가
        LocalDate today = LocalDate.now();
        LocalDate scheduleDate = schedule.getScheduleDate();
        if (scheduleDate.isBefore(today)) {
            throw new InvalidScheduleDateException(TaskErrorCode.CANNOT_UPDATE_PAST_SCHEDULE);
        }

        // 5. 카테고리 변경 요청이 있을 경우, 새 카테고리 존재 및 소유자 검증
        if (request.getCategoryId() != null && !request.getCategoryId().equals(schedule.getCategory().getCategoryId())) {
            Category newCategory = categoryCommandRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(TaskErrorCode.CATEGORY_NOT_FOUND));
            if (!newCategory.getMemberId().equals(memberId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 카테고리에 대한 권한이 없습니다.");
            }
            schedule.setCategory(newCategory);
        }

        // 6. 일정 정보 수정
        if (request.getScheduleContent() != null) {
            schedule.setScheduleContent(request.getScheduleContent());
        }
        if (request.getScheduleDate() != null) {
            // 입력받은 날짜가 오늘보다 과거면 예외 발생
            if (request.getScheduleDate().isBefore(today)) {
                throw new InvalidScheduleDateException(TaskErrorCode.INVALID_SCHEDULE_DATE);
            }
            schedule.setScheduleDate(request.getScheduleDate());
        }

        // 7. 저장
        Schedule updated = scheduleRepository.save(schedule);

        // 8. 응답 반환
        ScheduleCommandResponse.builder()
                .scheduleId(updated.getScheduleId())
                .categoryId(updated.getCategory().getCategoryId())
                .scheduleContent(updated.getScheduleContent())
                .scheduleDate(updated.getScheduleDate())
                .completionStatus(updated.getCompletionStatus())
                .build();
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long memberId) {
        // 1. 일정 조회
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(TaskErrorCode.SCHEDULE_NOT_FOUND));

        // 2. 소유자 검증
        if (!schedule.getCategory().getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "삭제 권한이 없습니다.");
        }

        // 3. 삭제
        scheduleRepository.delete(schedule);
    }

    @Transactional
    public ScheduleCommandResponse updateCompletionStatus(Long scheduleId, Boolean completionStatus, Long memberId) {
        // 1. 일정 조회
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(TaskErrorCode.SCHEDULE_NOT_FOUND));

        // 2. 소유자 검증
        if (!schedule.getCategory().getMemberId().equals(memberId)) {
            throw new UnauthorizedModificationException(TaskErrorCode.UNAUTHORIZED_MODIFICATION);
        }

        // 3. 당일 일정만 완료 처리 가능하도록 검증
        if (completionStatus != null && completionStatus) {
            LocalDate today = LocalDate.now();
            LocalDate scheduleDate = schedule.getScheduleDate();

            if (!scheduleDate.equals(today)) {
                throw new InvalidScheduleDateException(TaskErrorCode.CANNOT_COMPLETE_NON_TODAY_SCHEDULE);
            }
        }

        // 4. 상태 변경
        schedule.setCompletionStatus(completionStatus);

        // 5. 저장
        Schedule updated = scheduleRepository.save(schedule);

        // 6. 응답 반환
        return ScheduleCommandResponse.builder()
                .scheduleId(updated.getScheduleId())
                .categoryId(updated.getCategory().getCategoryId())
                .scheduleContent(updated.getScheduleContent())
                .scheduleDate(updated.getScheduleDate())
                .completionStatus(updated.getCompletionStatus())
                .build();
    }

    @Transactional
    public void deleteSchedulesByCategoryId(Long categoryId) {
        scheduleRepository.deleteByCategory_CategoryId(categoryId);
    }
}