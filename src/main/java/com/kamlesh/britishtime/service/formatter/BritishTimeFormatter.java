package com.kamlesh.britishtime.service.formatter;

import java.time.LocalTime;
import java.util.Objects;

/**
 * British spoken time formatter implementation.
 */
public class BritishTimeFormatter implements TimeSpokenFormatter {

    private static final String[] HOURS = {
        "twelve", "one", "two", "three", "four", "five", "six",
        "seven", "eight", "nine", "ten", "eleven", "twelve"
    };


    @Override
    public String format(LocalTime time) {
        Objects.requireNonNull(time, "time must not be null");
        int hour24 = time.getHour();
        int hour12 = hour24 % 12;
        int minute = time.getMinute();

        if (hour24 == 0 && minute == 0) return "midnight";
        if (hour24 == 12 && minute == 0) return "noon";
        if (minute == 0) return HOURS[hour12] + " o'clock";
        if (minute == 15) return "quarter past " + HOURS[hour12];
        if (minute == 30) return "half past " + HOURS[hour12];
        if (minute == 45) return "quarter to " + HOURS[(hour12 + 1) % 12];

        if (minute >= 31 && minute <= 34) {
            return HOURS[hour12] + " " + minuteToSpoken(minute);
        }

        if (minute > 30) {
            int toMinutes = 60 - minute;
            return minuteToSpoken(toMinutes) + " to " + HOURS[(hour12 + 1) % 12];
        } else {
            return minuteToSpoken(minute) + " past " + HOURS[hour12];
        }
    }

    private String minuteToSpoken(int minute) {
        if (minute < 20) return unitWords(minute);
        int tens = minute / 10;
        int ones = minute % 10;

        String tensWord = switch (tens) {
            case 2 -> "twenty";
            case 3 -> "thirty";
            case 4 -> "forty";
            case 5 -> "fifty";
            default -> "";
        };

        return ones == 0 ? tensWord : tensWord + " " + unitWords(ones);
    }

    private String unitWords(int n) {
        return switch (n) {
            case 1 -> "one";
            case 2 -> "two";
            case 3 -> "three";
            case 4 -> "four";
            case 5 -> "five";
            case 6 -> "six";
            case 7 -> "seven";
            case 8 -> "eight";
            case 9 -> "nine";
            case 10 -> "ten";
            case 11 -> "eleven";
            case 12 -> "twelve";
            case 13 -> "thirteen";
            case 14 -> "fourteen";
            case 15 -> "fifteen";
            case 16 -> "sixteen";
            case 17 -> "seventeen";
            case 18 -> "eighteen";
            case 19 -> "nineteen";
            case 20 -> "twenty";
            default -> Integer.toString(n);
        };
    }
}
