package com.kamlesh.britishtime.controller;

import com.kamlesh.britishtime.service.TimeService;
import com.kamlesh.britishtime.dtos.SpokenTimeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing the spoken time endpoint.
 */
@RestController
@RequestMapping("/api/time")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/spoken")
    public ResponseEntity<SpokenTimeResponse> spoken(@RequestParam("time") String time) {
        return ResponseEntity.ok(timeService.toSpokenTime(time));
    }
}
