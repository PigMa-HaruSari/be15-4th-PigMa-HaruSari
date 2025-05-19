package com.pigma.harusari.statistics.query.utils;

import com.pigma.harusari.statistics.query.exception.InvalidDateFormatException;
import com.pigma.harusari.statistics.query.exception.MissingDateException;
import com.pigma.harusari.statistics.query.exception.StatisticsErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StatisticsRequestParser {

    public static LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            throw new MissingDateException(StatisticsErrorCode.MISSING_DATE);
        }

        try {
            return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException(StatisticsErrorCode.INVALID_DATE_FORMAT);
        }
    }

}