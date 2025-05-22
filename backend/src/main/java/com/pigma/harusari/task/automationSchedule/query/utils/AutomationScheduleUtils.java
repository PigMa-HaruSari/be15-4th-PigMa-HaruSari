package com.pigma.harusari.task.automationSchedule.query.utils;

import com.pigma.harusari.task.automationSchedule.command.entity.AutomationSchedule;

import java.time.LocalDate;

public class AutomationScheduleUtils {

    public static String getRepeatCycleString(AutomationSchedule a) {
        switch (a.getRepeatType()) {
            case WEEKLY:
                return "매주 " + (a.getRepeatWeekdays() != null ? a.getRepeatWeekdays() : "");
            case MONTHLY:
                return "매월 " + (a.getRepeatMonthday() != null ? a.getRepeatMonthday() + "일" : "");
            case DAILY:
                return "매일";
            default:
                return "";
        }
    }

    public static LocalDate calculateNextScheduleDate(AutomationSchedule a, LocalDate today) {
        switch (a.getRepeatType()) {
            case DAILY:
                return today.plusDays(1);
            case WEEKLY:
                // 실제 요일 파싱 및 계산 필요 (여기서는 단순 예시)
                return today.plusWeeks(1);
            case MONTHLY:
                int day = a.getRepeatMonthday() != null ? a.getRepeatMonthday() : 1;
                LocalDate nextMonth = today.withDayOfMonth(1).plusMonths(1).withDayOfMonth(day);
                return nextMonth;
            default:
                return null;
        }
    }
}
