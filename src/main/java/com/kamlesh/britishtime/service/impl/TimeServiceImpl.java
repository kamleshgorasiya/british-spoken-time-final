package com.kamlesh.britishtime.service.impl;

import com.kamlesh.britishtime.service.TimeService;
import com.kamlesh.britishtime.service.formatter.TimeSpokenFormatter;
import org.springframework.stereotype.Service;
import java.time.LocalTime;

/**
 * © 2025 Kamlesh Gorasiya
 * Implementation of TimeService that delegates spoken time conversion
 * to a configurable TimeSpokenFormatter (Strategy pattern).
 */
@Service
public class TimeServiceImpl implements TimeService {

    private final TimeSpokenFormatter timeFormatter;

    public TimeServiceImpl(TimeSpokenFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    /**
     * Converts a given LocalTime to its spoken representation
     * using the configured TimeSpokenFormatter implementation.
     *
     * @param time the input time
     * @return spoken time (e.g., "quarter past four")
     */
    @Override
    public String toSpokenTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
        return timeFormatter.format(time);
    }
}
