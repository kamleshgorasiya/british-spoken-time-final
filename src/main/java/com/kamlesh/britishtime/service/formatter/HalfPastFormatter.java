package com.kamlesh.britishtime.service.formatter;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for half past the hour (e.g., "half past three").
 */
public class HalfPastFormatter extends AbstractTimeFormatter {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        if (time.getMinute() == 30) {
            int hour12 = time.getHour() % 12;
            return Optional.of("half past " + TimeWords.hourWord(hour12));
        }
        return Optional.empty();
    }
}
