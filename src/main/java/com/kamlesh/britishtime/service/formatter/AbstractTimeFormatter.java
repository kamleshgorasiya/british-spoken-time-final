package com.kamlesh.britishtime.service.formatter;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Abstract base class for time formatters using Chain of Responsibility pattern.
 * Each formatter can handle specific time conditions and delegate to the next formatter if it cannot handle.
 */
public abstract class AbstractTimeFormatter implements TimeSpokenFormatter {

    protected AbstractTimeFormatter nextFormatter;

    /**
     * Sets the next formatter in the chain.
     *
     * @param nextFormatter the next formatter to delegate to
     * @return this formatter for method chaining
     */
    public AbstractTimeFormatter setNext(AbstractTimeFormatter nextFormatter) {
        this.nextFormatter = nextFormatter;
        return nextFormatter;
    }

    @Override
    public String format(LocalTime time) {
        Optional<String> result = tryFormat(time);
        if (result.isPresent()) {
            return result.get();
        }
        if (nextFormatter != null) {
            return nextFormatter.format(time);
        }
        throw new IllegalStateException("No formatter could handle time: " + time);
    }

    /**
     * Attempts to format the time. Returns Optional.empty() if this formatter cannot handle it.
     *
     * @param time the time to format
     * @return Optional containing the formatted string, or empty if this formatter cannot handle it
     */
    protected abstract Optional<String> tryFormat(LocalTime time);
}
