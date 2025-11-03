package com.kamlesh.britishtime.config;

import com.kamlesh.britishtime.service.formatter.TimeFormatterFactory;
import com.kamlesh.britishtime.service.formatter.TimeSpokenFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * Demonstrates bean creation for locale-based formatters. For the exercise, 'en-GB' returns BritishTimeFormatter.
 */
@Configuration
public class FormatterConfiguration {

    @Value("${app.locale:en-GB}")
    private String locale;

    @Bean
    public TimeSpokenFormatter timeSpokenFormatter() {
        return TimeFormatterFactory.defaultFormatter();
    }
}
