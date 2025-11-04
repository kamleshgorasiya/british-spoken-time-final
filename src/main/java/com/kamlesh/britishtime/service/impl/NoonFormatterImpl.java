package com.kamlesh.britishtime.service.impl;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for noon (12:00).
 */
public class NoonFormatterImpl extends AbstractTimeFormatterImpl {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        if (time.getHour() == 12 && time.getMinute() == 0) {
            return Optional.of("noon");
        }
        return Optional.empty();
    }
}
