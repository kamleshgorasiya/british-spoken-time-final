package com.kamlesh.britishtime;

import com.kamlesh.britishtime.service.formatter.TimeFormatterFactory;
import com.kamlesh.britishtime.utility.TimeParser;

import java.time.LocalTime;

/**
 * Minimal CLI entrypoint. If run as a fat jar without spring-boot:run, this can be used.
 */
public class BritishSpokenTimeApp {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar **/britishtime-cli-1.0.0.jar <HH:mm>");
            return;
        }
        LocalTime time = TimeParser.parse(args[0]);
        System.out.println(TimeFormatterFactory.defaultFormatter().format(time));
    }
}
