package com.kamlesh.britishtime.service.formatter;

import java.time.LocalTime;
import java.util.Objects;

/**
 * British spoken time formatter implementation.
 */
public class BritishTimeFormatter implements TimeSpokenFormatter {

    @Override
    public String format(LocalTime time) {
        Objects.requireNonNull(time, "time must not be null");
        int hour24 = time.getHour();
        int hour12 = hour24 % 12;
        int minute = time.getMinute();

        if (hour24 == 0 && minute == 0) return "midnight";
        if (hour24 == 12 && minute == 0) return "noon";
        if (minute == 0) return TimeWords.hourWord(hour12) + " o'clock";
        if (minute == 15) return "quarter past " + TimeWords.hourWord(hour12);
        if (minute == 30) return "half past " + TimeWords.hourWord(hour12);
        if (minute == 45) return "quarter to " + TimeWords.hourWord((hour12 + 1) % 12);

        if (minute >= 31 && minute <= 34) {
            return TimeWords.hourWord(hour12) + " " + minuteToSpoken(minute);
        }

        if (minute > 30) {
            int toMinutes = 60 - minute;
            return minuteToSpoken(toMinutes) + " to " + TimeWords.hourWord((hour12 + 1) % 12);
        } else {
            return minuteToSpoken(minute) + " past " + TimeWords.hourWord(hour12);
        }
    }

    private String minuteToSpoken(int minute) {
        if (minute < 20) return TimeWords.unitWord(minute);

        int tens = minute / 10;
        int ones = minute % 10;

        String tensWord = TimeWords.tensWord(tens);
        return ones == 0 ? tensWord : tensWord + " " + TimeWords.unitWord(ones);
    }
}