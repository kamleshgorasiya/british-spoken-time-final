package com.kamlesh.britishtime;

import com.kamlesh.britishtime.dtos.SpokenTimeResponse;
import com.kamlesh.britishtime.service.TimeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entrypoint for the demo.
 */
@SpringBootApplication
public class BritishSpokenTimeApplication {

    //private final TimeService timeService;

    /*public BritishSpokenTimeApplication(TimeService timeService) {
        this.timeService = timeService;
    }*/

    public static void main(String[] args) {
        SpringApplication.run(BritishSpokenTimeApplication.class, args);
    }

   /* @Override
    public void run(String... args) throws Exception {
        *//*LocalTime time = TimeParser.parse(args[0]);
        System.out.println(TimeFormatterFactory.defaultFormatter().format(time));*//*
        SpokenTimeResponse spokenTime = timeService.toSpokenTime("12:30");
        spokenTime = timeService.toSpokenTime("12:30");
        System.out.println(spokenTime);
        spokenTime = timeService.toSpokenTime("12:00");
        System.out.println(spokenTime);
        spokenTime = timeService.toSpokenTime("00:00");
        System.out.println(spokenTime);
        spokenTime = timeService.toSpokenTime("05:20");
        System.out.println(spokenTime);
        spokenTime = timeService.toSpokenTime("0632");
        System.out.println(spokenTime);
    }*/
}
