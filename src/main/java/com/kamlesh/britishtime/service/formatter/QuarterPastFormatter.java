package com.kamlesh.britishtime.service.formatter;

import com.kamlesh.britishtime.utility.TimeWords;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for quarter past the hour (e.g., "quarter past three").
 */
public class QuarterPastFormatter extends AbstractTimeFormatter {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        if (time.getMinute() == 15) {
            int hour12 = time.getHour() % 12;
            return Optional.of("quarter past " + TimeWords.hourWord(hour12));
        }
        return Optional.empty();
    }
}
