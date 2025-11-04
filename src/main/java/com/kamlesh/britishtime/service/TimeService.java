package com.kamlesh.britishtime.service;

import com.kamlesh.britishtime.dtos.SpokenTimeResponse;

/**
 * © 2025 Kamlesh Gorasiya
 * Defines the contract for converting a LocalTime into its
 * spoken (British English) representation.
 * Implementations of this interface should use the Strategy Pattern
 * to delegate formatting logic to specific TimeSpokenFormatter instances.
 */
public interface TimeService {

    /**
     * Converts a given LocalTime into a spoken representation of time.
     * <p>
     * Examples:
     * <ul>
     *     <li>09:00 → "nine o'clock"</li>
     *     <li>09:15 → "quarter past nine"</li>
     *     <li>09:30 → "half past nine"</li>
     *     <li>09:45 → "quarter to ten"</li>
     * </ul>
     *
     * @param time LocalTime instance to convert (not null)
     * @return the spoken form of the time in British English
     * @throws IllegalArgumentException if time is null
     */
    SpokenTimeResponse toSpokenTime(String time);
}
