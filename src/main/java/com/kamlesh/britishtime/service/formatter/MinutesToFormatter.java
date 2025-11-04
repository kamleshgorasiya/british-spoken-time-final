package com.kamlesh.britishtime.service.formatter;

import com.kamlesh.britishtime.utility.TimeWords;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for minutes to the next hour (e.g., "five to four", "twenty to five").
 * Handles minutes from 31-59 (excluding special cases like 45).
 * Special handling for 31-34 minutes which use "past" format.
 */
public class MinutesToFormatter extends AbstractTimeFormatter {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        int minute = time.getMinute();
        int hour12 = time.getHour() % 12;
        
        // Special case: 31-34 minutes use "past" format
        if (minute >= 31 && minute <= 34) {
            String minuteSpoken = minuteToSpoken(minute);
            return Optional.of(TimeWords.hourWord(hour12) + " " + minuteSpoken);
        }
        
        // Handle minutes 35-59, excluding 45
        if (minute > 34 && minute < 60 && minute != 45) {
            int toMinutes = 60 - minute;
            int nextHour = (hour12 + 1) % 12;
            String minuteSpoken = minuteToSpoken(toMinutes);
            return Optional.of(minuteSpoken + " to " + TimeWords.hourWord(nextHour));
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
