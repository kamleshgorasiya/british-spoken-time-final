package com.kamlesh.britishtime.service.impl;

import com.kamlesh.britishtime.utility.TimeWords;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for minutes to the next hour (e.g., "five to four", "twenty to five").
 * Handles minutes from 35-59 (excluding 45 which is handled by QuarterToFormatter).
 */
public class MinutesToFormatterImpl extends AbstractTimeFormatterImpl {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        int minute = time.getMinute();
        
        // Handle minutes 35-59, excluding 45
        if (minute >= 35 && minute != 45) {
            int hour12 = time.getHour() % 12;
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
