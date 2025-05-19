package com.pigma.harusari.statistics.query.utils;

import com.pigma.harusari.statistics.query.exception.InvalidDateFormatException;
import com.pigma.harusari.statistics.query.exception.MissingDateException;
import com.pigma.harusari.statistics.query.exception.StatisticsErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[통계 - utils] StatisticsRequestParser 테스트")
class StatisticsRequestParserTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    @DisplayName("[예외] 날짜 값이 경우 예외가 발생하는 테스트")
    void parseDateMissingDateException(String condition) {
        assertThatThrownBy(() -> StatisticsRequestParser.parseDate(condition))
                .isInstanceOf(MissingDateException.class)
                .hasMessage(StatisticsErrorCode.MISSING_DATE.getErrorMessage());
    }

    @ParameterizedTest
    @CsvSource({"2025.01.01", "2025-01-01 15:30:15"})
    @DisplayName("[예외] 날짜 형식에 맞지 않은 경우 예외가 발생하는 테스트")
    void parseDateInvalidDateFormatException(String condition) {
        assertThatThrownBy(() -> StatisticsRequestParser.parseDate(condition))
                .isInstanceOf(InvalidDateFormatException.class)
                .hasMessage(StatisticsErrorCode.INVALID_DATE_FORMAT.getErrorMessage());
    }

}