package com.pigma.harusari.task.automationSchedule.command.service;

import com.pigma.harusari.category.command.domain.aggregate.Category;
import com.pigma.harusari.category.command.domain.repository.CategoryCommandRepository;
import com.pigma.harusari.task.automationSchedule.command.dto.request.AutomationScheduleCreateRequest;
import com.pigma.harusari.task.automationSchedule.command.dto.response.AutomationScheduleResponse;
import com.pigma.harusari.task.automationSchedule.command.entity.AutomationSchedule;
import com.pigma.harusari.task.automationSchedule.command.entity.RepeatType;
import com.pigma.harusari.task.automationSchedule.command.repository.AutomationScheduleRepository;
import com.pigma.harusari.task.exception.*;
import com.pigma.harusari.task.schedule.command.dto.request.ScheduleCreateRequest;
import com.pigma.harusari.task.schedule.command.repository.ScheduleRepository;
import com.pigma.harusari.task.schedule.command.service.ScheduleCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutomationScheduleServiceImpl implements AutomationScheduleService {

    private final AutomationScheduleRepository automationScheduleRepository;
    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleRepository scheduleRepository;
    private final CategoryCommandRepository categoryCommandRepository;

    // 자동화 일정 생성 (사용자 요청)
    @Transactional
    public Long createAutomationSchedule(AutomationScheduleCreateRequest request, Long memberId) {
        Category category = categoryCommandRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(TaskErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to create schedule for this category");
        }

        AutomationSchedule automationSchedule = AutomationSchedule.builder()
                .categoryId(request.getCategoryId())
                .automationScheduleContent(request.getAutomationScheduleContent())
                .endDate(request.getEndDate())
                .repeatType(RepeatType.valueOf(request.getRepeatType()))
                .repeatWeekdays(request.getRepeatWeekdays())
                .repeatMonthday(request.getRepeatMonthday())
                .build();

        automationScheduleRepository.save(automationSchedule);

        // 자동화 일정 등록 시, 즉시 실제 일정 생성
        generateSchedulesForAutomation(automationSchedule, LocalDate.now());

        return automationSchedule.getAutomationScheduleId();
    }

    // 자동화 일정 생성 (매일 0시)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void generateSchedules() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        List<AutomationSchedule> automations = automationScheduleRepository.findByEndDateAfter(today);

        for (AutomationSchedule automation : automations) {
            try {
                generateSchedulesForAutomation(automation, today);
            } catch (Exception e) {
                log.error("자동화 스케줄 생성 실패: id={}, error={}", automation.getAutomationScheduleId(), e.getMessage(), e);
            }
        }
    }

    // 일정 생성 로직 (today ~ endDate)
    public void generateSchedulesForAutomation(AutomationSchedule automationSchedule, LocalDate startDate) {
        LocalDate current = startDate;
        LocalDate end = automationSchedule.getEndDate();

        while (current.isBefore(end)) {
            current = calculateNextDate(current, automationSchedule);

            if (current.isAfter(end)) break;

            createSchedule(automationSchedule, current);
        }
    }

    private LocalDate calculateNextDate(LocalDate current, AutomationSchedule automation) {
        RepeatType type = automation.getRepeatType();
        switch (type) {
            case DAILY:
                return current.plusDays(1);
            case WEEKLY:
                validateWeekdays(automation.getRepeatWeekdays());
                LocalDate next = current.plusDays(1);
                while (!isValidWeekday(next, automation.getRepeatWeekdays())) {
                    next = next.plusDays(1);
                }
                return next;
            case MONTHLY:
                Integer targetDay = automation.getRepeatMonthday();
                if (targetDay == null || targetDay < 1 || targetDay > 31) {
                    throw new InvalidRepeatTypeException(TaskErrorCode.INVALID_REPEAT_MONTHDAY);
                }
                LocalDate nextMonth = current.plusMonths(1);
                return adjustMonthday(nextMonth, targetDay);
            default:
                throw new InvalidRepeatTypeException(TaskErrorCode.INVALID_REPEAT_TYPE);
        }
    }

    private void validateWeekdays(String weekdays) {
        if (weekdays == null || weekdays.isBlank()) {
            throw new InvalidRepeatTypeException(TaskErrorCode.INVALID_REPEAT_WEEKDAYS);
        }
    }

    private boolean isValidWeekday(LocalDate date, String weekdays) {
        String dayAbbr = date.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                .substring(0, 2)
                .toLowerCase();
        return weekdays.toLowerCase().contains(dayAbbr);
    }

    private LocalDate adjustMonthday(LocalDate date, int targetDay) {
        int maxDay = date.lengthOfMonth();
        int adjustedDay = Math.min(targetDay, maxDay);
        return date.withDayOfMonth(adjustedDay);
    }

    private void createSchedule(AutomationSchedule automationSchedule, LocalDate date) {
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .categoryId(automationSchedule.getCategoryId())
                .scheduleContent(automationSchedule.getAutomationScheduleContent())
                .scheduleDate(date)
                .automationScheduleId(automationSchedule.getAutomationScheduleId())
                .build();
        scheduleCommandService.createSchedule(scheduleCreateRequest);
    }

    @Transactional
    public void updateAutomationSchedule(Long scheduleId, AutomationScheduleCreateRequest request, Long memberId) {
        AutomationSchedule schedule = automationScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        Category category = categoryCommandRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (!category.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this schedule");
        }

        // 필드 업데이트
        schedule.updateCategoryId(request.getCategoryId());
        schedule.updateContent(request.getAutomationScheduleContent());
        schedule.updateEndDate(request.getEndDate());
        schedule.updateRepeatType(RepeatType.valueOf(request.getRepeatType()));
        schedule.updateRepeatWeekdays(request.getRepeatWeekdays());
        schedule.updateRepeatMonthday(request.getRepeatMonthday());

        // 기준일을 오늘로 고정
        LocalDate now = LocalDate.now();

        // 오늘 이후 일정 삭제
        scheduleCommandService.deleteSchedulesAfter(schedule.getAutomationScheduleId(), now);

        // 오늘 ~ 종료일 구간의 실제 일정 재생성
        generateSchedulesForAutomation(schedule, now);
    }

    @Transactional
    public void deleteSchedulesAfter(Long automationScheduleId, Long memberId) {
        // 1. 자동화 일정 조회
        AutomationSchedule automationSchedule = automationScheduleRepository.findById(automationScheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(TaskErrorCode.SCHEDULE_NOT_FOUND));

        // 2. 카테고리 조회 및 소유자(memberId) 검증
        Category category = categoryCommandRepository.findById(automationSchedule.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(TaskErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 일정을 삭제할 권한이 없습니다.");
        }

        // 3. 오늘 이후 일정 삭제
        LocalDate now = LocalDate.now();
        scheduleRepository.deleteByAutomationSchedule_AutomationScheduleIdAndScheduleDateAfter(automationScheduleId, LocalDate.now());
    }

    public AutomationScheduleResponse getAutomationSchedule(Long automationScheduleId, Long memberId) {
        AutomationSchedule automationSchedule = automationScheduleRepository.findById(automationScheduleId)
                .orElseThrow(() -> new AutomationScheduleNotFoundException(TaskErrorCode.AUTOMATION_SCHEDULE_NOT_FOUND));

        // 소유자 검증
        if (automationScheduleId != null) {
            automationSchedule = automationScheduleRepository.findById(automationScheduleId)
                    .orElseThrow(() -> new AutomationScheduleNotFoundException(TaskErrorCode.AUTOMATION_SCHEDULE_NOT_FOUND));
        }

        return AutomationScheduleResponse.builder()
                .automationScheduleId(automationSchedule.getAutomationScheduleId())
                .categoryId(automationSchedule.getCategoryId())
                .automationScheduleContent(automationSchedule.getAutomationScheduleContent())
                .endDate(automationSchedule.getEndDate())
                .repeatType(automationSchedule.getRepeatType())
                .repeatWeekdays(automationSchedule.getRepeatWeekdays())
                .repeatMonthday(automationSchedule.getRepeatMonthday())
                .build();
    }

}