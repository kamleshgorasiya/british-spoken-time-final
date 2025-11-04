package com.kamlesh.britishtime.service.impl;

import com.kamlesh.britishtime.utility.TimeWords;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for exact hours (e.g., "three o'clock").
 */
public class OClockFormatterImpl extends AbstractTimeFormatterImpl {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        if (time.getMinute() == 0) {
            int hour12 = time.getHour() % 12;
            return Optional.of(TimeWords.hourWord(hour12) + " o'clock");
        }
        return Optional.empty();
    }
}
