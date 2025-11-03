package com.kamlesh.britishtime.utility;

import com.kamlesh.britishtime.exception.InvalidTimeFormatException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Parses HH:mm input into a LocalTime.
 */
public final class TimeParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private TimeParser() {}
    public static LocalTime parse(String input) {
        Objects.requireNonNull(input, "input must not be null");
        try {
            return LocalTime.parse(input, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidTimeFormatException("Invalid time format. Expected HH:mm, got: " + input);
        }
    }
}
