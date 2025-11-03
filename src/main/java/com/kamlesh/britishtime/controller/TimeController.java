package com.kamlesh.britishtime.controller;

import com.kamlesh.britishtime.exception.InvalidTimeFormatException;
import com.kamlesh.britishtime.service.formatter.TimeSpokenFormatter;
import com.kamlesh.britishtime.utility.TimeParser;
import com.kamlesh.britishtime.model.SpokenTimeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * REST controller exposing the spoken time endpoint.
 */
@RestController
@RequestMapping("/api/time")
public class TimeController {

    private final TimeSpokenFormatter formatter;

    public TimeController(TimeSpokenFormatter formatter) {
        this.formatter = formatter;
    }

    @GetMapping("/spoken")
    public ResponseEntity<SpokenTimeResponse> spoken(@RequestParam("time") String time) {
        try {
            LocalTime t = TimeParser.parse(time);
            String spoken  = formatter.format(t);
            return ResponseEntity.ok(new SpokenTimeResponse(time, spoken));
        } catch (DateTimeParseException ex) {
            throw new InvalidTimeFormatException("Invalid time format. Please use HH:mm (e.g., 09:30).");
        }
    }
}
