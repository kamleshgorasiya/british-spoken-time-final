package com.kamlesh.britishtime.service.impl;

import com.kamlesh.britishtime.dtos.SpokenTimeResponse;
import com.kamlesh.britishtime.exception.InvalidTimeFormatException;
import com.kamlesh.britishtime.service.TimeService;
import com.kamlesh.britishtime.service.formatter.TimeSpokenFormatter;
import com.kamlesh.britishtime.utility.TimeParser;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

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
    public SpokenTimeResponse toSpokenTime(String time) {
        try {
            LocalTime t = TimeParser.parse(time);
            String spoken  = timeFormatter.format(t);
            return new SpokenTimeResponse(time, spoken);
        } catch (DateTimeParseException ex) {
            throw new InvalidTimeFormatException("Invalid time format. Please use HH:mm (e.g., 09:30).");
        }
    }
}
