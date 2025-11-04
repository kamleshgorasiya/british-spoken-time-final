package com.kamlesh.britishtime.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds spoken word mappings for hours and numbers.
 */
public final class TimeWords {

    private static final Map<Integer, String> HOURS_MAP = new HashMap<>();
    private static final Map<Integer, String> UNIT_WORDS_MAP = new HashMap<>();
    private static final Map<Integer, String> TENS_WORDS_MAP = new HashMap<>();

    static {
        HOURS_MAP.put(0, "twelve");
        HOURS_MAP.put(1, "one");
        HOURS_MAP.put(2, "two");
        HOURS_MAP.put(3, "three");
        HOURS_MAP.put(4, "four");
        HOURS_MAP.put(5, "five");
        HOURS_MAP.put(6, "six");
        HOURS_MAP.put(7, "seven");
        HOURS_MAP.put(8, "eight");
        HOURS_MAP.put(9, "nine");
        HOURS_MAP.put(10, "ten");
        HOURS_MAP.put(11, "eleven");
        HOURS_MAP.put(12, "twelve");

        String[] unitWords = {
            "zero", "one", "two", "three", "four", "five", "six",
            "seven", "eight", "nine", "ten", "eleven", "twelve",
            "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
            "eighteen", "nineteen"
        };

        for (int i = 0; i < unitWords.length; i++) {
            UNIT_WORDS_MAP.put(i, unitWords[i]);
        }

        TENS_WORDS_MAP.put(2, "twenty");
        TENS_WORDS_MAP.put(3, "thirty");
        TENS_WORDS_MAP.put(4, "forty");
        TENS_WORDS_MAP.put(5, "fifty");
    }

    private TimeWords() {
    }

    public static String hourWord(int hour12) {
        return HOURS_MAP.getOrDefault(hour12, "");
    }

    public static String unitWord(int n) {
        return UNIT_WORDS_MAP.getOrDefault(n, Integer.toString(n));
    }

    public static String tensWord(int tens) {
        return TENS_WORDS_MAP.getOrDefault(tens, "");
    }
}
