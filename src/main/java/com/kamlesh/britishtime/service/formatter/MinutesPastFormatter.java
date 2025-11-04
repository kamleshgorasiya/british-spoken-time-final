package com.kamlesh.britishtime.service.formatter;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for minutes past the hour (e.g., "five past three", "twenty-three past four").
 * Handles minutes from 1-30 (excluding special cases like 15 and 30).
 */
public class MinutesPastFormatter extends AbstractTimeFormatter {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        int minute = time.getMinute();
        
        // Handle minutes 1-30, excluding special cases (15, 30)
        if (minute > 0 && minute <= 30 && minute != 15 && minute != 30) {
            int hour12 = time.getHour() % 12;
            String minuteSpoken = minuteToSpoken(minute);
            return Optional.of(minuteSpoken + " past " + TimeWords.hourWord(hour12));
        }
        
        return Optional.empty();
    }

    private String minuteToSpoken(int minute) {
        if (minute < 20) {
            return TimeWords.unitWord(minute);
        }

        int tens = minute / 10;
        int ones = minute % 10;

        String tensWord = TimeWords.tensWord(tens);
        return ones == 0 ? tensWord : tensWord + " " + TimeWords.unitWord(ones);
    }
}
