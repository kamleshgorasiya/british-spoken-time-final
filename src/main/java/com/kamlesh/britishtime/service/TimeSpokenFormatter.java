package com.kamlesh.britishtime.service;

import java.time.LocalTime;

/**
 * Strategy interface for converting a given time (HH:mm)
 * into its spoken British English form.
 */
public interface TimeSpokenFormatter {

    String format(LocalTime time);
}
