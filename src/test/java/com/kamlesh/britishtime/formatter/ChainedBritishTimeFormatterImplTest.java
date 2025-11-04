package com.kamlesh.britishtime.formatter;

import com.kamlesh.britishtime.service.TimeSpokenFormatter;
import com.kamlesh.britishtime.service.impl.*;
import com.kamlesh.britishtime.service.impl.MidnightFormatterImpl;
import com.kamlesh.britishtime.service.impl.OClockFormatterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ChainedBritishTimeFormatter and individual formatters.
 */
class ChainedBritishTimeFormatterImplTest {

    private TimeSpokenFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new ChainedBritishTimeFormatterImpl();
    }

    @ParameterizedTest
    @CsvSource({
        "00:00, midnight",
        "12:00, noon",
        "01:00, one o'clock",
        "03:00, three o'clock",
        "09:00, nine o'clock",
        "15:00, three o'clock",
        "23:00, eleven o'clock"
    })
    void testSpecialTimes(String timeStr, String expected) {
        LocalTime time = LocalTime.parse(timeStr);
        assertEquals(expected, formatter.format(time));
    }

    @ParameterizedTest
    @CsvSource({
        "03:15, quarter past three",
        "09:15, quarter past nine",
        "15:15, quarter past three",
        "23:15, quarter past eleven"
    })
    void testQuarterPast(String timeStr, String expected) {
        LocalTime time = LocalTime.parse(timeStr);
        assertEquals(expected, formatter.format(time));
    }

    @ParameterizedTest
    @CsvSource({
        "03:30, half past three",
        "09:30, half past nine",
        "15:30, half past three",
        "23:30, half past eleven"
    })
    void testHalfPast(String timeStr, String expected) {
        LocalTime time = LocalTime.parse(timeStr);
        assertEquals(expected, formatter.format(time));
    }

    @ParameterizedTest
    @CsvSource({
        "03:45, quarter to four",
        "09:45, quarter to ten",
        "15:45, quarter to four",
        "23:45, quarter to twelve"
    })
    void testQuarterTo(String timeStr, String expected) {
        LocalTime time = LocalTime.parse(timeStr);
        assertEquals(expected, formatter.format(time));
    }

    @ParameterizedTest
    @CsvSource({
        "03:05, five past three",
        "03:10, ten past three",
        "03:20, twenty past three",
        "03:25, twenty five past three"
    })
    void testMinutesPast(String timeStr, String expected) {
        LocalTime time = LocalTime.parse(timeStr);
        assertEquals(expected, formatter.format(time));
    }

    @ParameterizedTest
    @CsvSource({
        "03:35, twenty five to four",
        "03:40, twenty to four",
        "03:50, ten to four",
        "03:55, five to four"
    })
    void testMinutesTo(String timeStr, String expected) {
        LocalTime time = LocalTime.parse(timeStr);
        assertEquals(expected, formatter.format(time));
    }

    @Test
    void testNullTimeThrowsException() {
        assertThrows(NullPointerException.class, () -> formatter.format(null));
    }

    @Test
    void testCustomChainWithBuilder() {
        TimeSpokenFormatter customFormatter = new ChainedBritishTimeFormatterImpl.Builder()
                .addFormatter(new MidnightFormatterImpl())
                .addFormatter(new NoonFormatterImpl())
                .addFormatter(new OClockFormatterImpl())
                .build();

        assertEquals("midnight", customFormatter.format(LocalTime.of(0, 0)));
        assertEquals("noon", customFormatter.format(LocalTime.of(12, 0)));
        assertEquals("three o'clock", customFormatter.format(LocalTime.of(3, 0)));
    }

    @Test
    void testBuilderThrowsExceptionWhenEmpty() {
        ChainedBritishTimeFormatterImpl.Builder builder = new ChainedBritishTimeFormatterImpl.Builder();
        assertThrows(IllegalStateException.class, builder::build);
    }
}
