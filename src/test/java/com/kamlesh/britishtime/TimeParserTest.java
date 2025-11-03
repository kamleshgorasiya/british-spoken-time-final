package com.kamlesh.britishtime;

import com.kamlesh.britishtime.exception.InvalidTimeFormatException;
import com.kamlesh.britishtime.utility.TimeParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeParserTest {

    @Test
    void invalidFormatShouldThrow() {
        assertThrows(InvalidTimeFormatException.class, () -> TimeParser.parse("25:00"));
    }
}
