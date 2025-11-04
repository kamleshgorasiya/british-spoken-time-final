package com.kamlesh.britishtime.service.impl;

import com.kamlesh.britishtime.utility.TimeWords;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for quarter to the hour (e.g., "quarter to four").
 */
public class QuarterToFormatterImpl extends AbstractTimeFormatterImpl {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        if (time.getMinute() == 45) {
            int hour12 = time.getHour() % 12;
            int nextHour = (hour12 + 1) % 12;
            return Optional.of("quarter to " + TimeWords.hourWord(nextHour));
        }
        return Optional.empty();
    }
}
