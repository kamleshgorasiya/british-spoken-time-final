package com.kamlesh.britishtime.service.formatter;

/**
 * Simple factory returning default formatter.
 */
public final class TimeFormatterFactory {
    private TimeFormatterFactory() {}
    public static TimeSpokenFormatter defaultFormatter() {
        return new BritishTimeFormatter();
    }
}
