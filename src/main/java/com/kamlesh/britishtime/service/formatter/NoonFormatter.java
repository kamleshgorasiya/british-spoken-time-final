package com.kamlesh.britishtime.service.formatter;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for noon (12:00).
 */
public class NoonFormatter extends AbstractTimeFormatter {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        if (time.getHour() == 12 && time.getMinute() == 0) {
            return Optional.of("noon");
        }
        return Optional.empty();
    }
}
