package com.kamlesh.britishtime.service.impl;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for midnight (00:00).
 */
public class MidnightFormatterImpl extends AbstractTimeFormatterImpl {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        if (time.getHour() == 0 && time.getMinute() == 0) {
            return Optional.of("midnight");
        }
        return Optional.empty();
    }
}
