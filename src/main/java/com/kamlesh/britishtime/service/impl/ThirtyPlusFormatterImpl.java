package com.kamlesh.britishtime.service.impl;

import com.kamlesh.britishtime.utility.TimeWords;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Formatter for 31-34 minutes (e.g., "three thirty one", "three thirty two").
 * Special case that uses "hour + minute" format instead of "to" format.
 */
public class ThirtyPlusFormatterImpl extends AbstractTimeFormatterImpl {

    @Override
    public Optional<String> tryFormat(LocalTime time) {
        int minute = time.getMinute();
        
        if (minute >= 31 && minute <= 34) {
            int hour12 = time.getHour() % 12;
            String minuteSpoken = minuteToSpoken(minute);
            return Optional.of(TimeWords.hourWord(hour12) + " " + minuteSpoken);
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
