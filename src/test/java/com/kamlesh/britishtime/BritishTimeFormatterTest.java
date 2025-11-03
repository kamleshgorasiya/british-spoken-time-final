package com.kamlesh.britishtime;

import com.kamlesh.britishtime.service.formatter.BritishTimeFormatter;
import com.kamlesh.britishtime.utility.TimeParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BritishTimeFormatterTest {

    private final BritishTimeFormatter formatter = new BritishTimeFormatter();

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({
        "01:00,one o'clock",
        "02:05,five past two",
        "03:10,ten past three",
        "04:15,quarter past four",
        "05:20,twenty past five",
        "06:25,twenty five past six",
        "06:32,six thirty two",
        "07:30,half past seven",
        "07:35,twenty five to eight",
        "08:40,twenty to nine",
        "09:45,quarter to ten",
        "10:50,ten to eleven",
        "11:55,five to twelve",
        "00:00,midnight",
        "12:00,noon"
    })
    void testSpokenTimes(String input, String expected) {
        LocalTime time = TimeParser.parse(input);
        assertEquals(expected, formatter.format(time));
    }

    @Test
    void shouldThrowExceptionForNullTime() {
        assertThrows(NullPointerException.class, () -> formatter.format(null));
    }

    @Test
    void shouldHandleEdgeHourTransitions() {
        assertEquals("quarter to one", formatter.format(LocalTime.of(12, 45)));
        assertEquals("quarter to one", formatter.format(LocalTime.of(0, 45)));
        assertEquals("five to twelve", formatter.format(LocalTime.of(11, 55)));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({
            "06:31,six thirty one",
            "06:32,six thirty two",
            "06:33,six thirty three",
            "06:34,six thirty four"
    })
    void shouldHandleMinutesPastThirtyOneToThirtyFourSpecially(String input, String expected) {
        var time = LocalTime.parse(input);
        assertEquals(expected, formatter.format(time));
    }

    @Test
    void shouldHandleExactHalfPastAndQuarterPast() {
        assertEquals("half past eight", formatter.format(LocalTime.of(8, 30)));
        assertEquals("quarter past eight", formatter.format(LocalTime.of(8, 15)));
    }

    @Test
    void shouldHandleMinutesToNextHour() {
        assertEquals("five to eight", formatter.format(LocalTime.of(7, 55)));
        assertEquals("twenty to eight", formatter.format(LocalTime.of(7, 40)));
        assertEquals("quarter to eight", formatter.format(LocalTime.of(7, 45)));
    }

    @Test
    void shouldHandleSingleDigitMinutesPast() {
        assertEquals("one past three", formatter.format(LocalTime.of(3, 1)));
        assertEquals("nine past three", formatter.format(LocalTime.of(3, 9)));
    }

    @Test
    void shouldHandleMinutesExactlyTwentyFiveOrTwenty() {
        assertEquals("twenty past six", formatter.format(LocalTime.of(6, 20)));
        assertEquals("twenty five past six", formatter.format(LocalTime.of(6, 25)));
    }

    @Test
    void shouldHandleEveningHoursCorrectly() {
        assertEquals("nine o'clock", formatter.format(LocalTime.of(21, 0)));
        assertEquals("quarter to ten", formatter.format(LocalTime.of(21, 45)));
    }

    @Test
    void shouldHandleNoonAndMidnightSpecialCases() {
        assertEquals("noon", formatter.format(LocalTime.of(12, 0)));
        assertEquals("midnight", formatter.format(LocalTime.of(0, 0)));
    }

    @Test
    void shouldReturnDifferentOutputForDifferentTimes() {
        LocalTime time1 = LocalTime.of(5, 20);
        LocalTime time2 = LocalTime.of(6, 40);
        assertNotEquals(formatter.format(time1), formatter.format(time2));
    }
}
