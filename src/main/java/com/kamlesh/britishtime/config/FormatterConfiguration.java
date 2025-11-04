package com.kamlesh.britishtime.config;

import com.kamlesh.britishtime.service.formatter.ChainedBritishTimeFormatter;
import com.kamlesh.britishtime.service.TimeSpokenFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration for time formatters.
 * Uses Chain of Responsibility pattern to handle different time formatting rules.
 */
@Configuration
public class FormatterConfiguration {

    @Value("${app.locale:en-GB}")
    private String locale;

    @Bean
    public TimeSpokenFormatter timeSpokenFormatter() {
        // Use the new chained formatter with specialized formatters
        return new ChainedBritishTimeFormatter();
    }
}
