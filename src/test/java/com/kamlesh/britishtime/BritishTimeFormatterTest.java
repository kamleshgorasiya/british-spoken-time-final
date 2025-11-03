package com.kamlesh.britishtime;

import com.kamlesh.britishtime.service.formatter.BritishTimeFormatter;
import com.kamlesh.britishtime.utility.TimeParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
}
