package com.kamlesh.britishtime.service.formatter;

import com.kamlesh.britishtime.service.TimeSpokenFormatter;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Composite formatter that chains multiple specialized formatters using Chain of Responsibility pattern.
 * This allows for clean separation of concerns where each formatter handles specific time conditions.
 * 
 * The chain order is important:
 * 1. Midnight (00:00)
 * 2. Noon (12:00)
 * 3. O'Clock (exact hours)
 * 4. Quarter Past (15 minutes)
 * 5. Half Past (30 minutes)
 * 6. Thirty Plus (31-34 minutes)
 * 7. Quarter To (45 minutes)
 * 8. Minutes Past (1-30 minutes)
 * 9. Minutes To (35-59 minutes)
 */
public class ChainedBritishTimeFormatter implements TimeSpokenFormatter {

    private final AbstractTimeFormatter formatterChain;

    /**
     * Creates a new ChainedBritishTimeFormatter with the default chain of formatters.
     */
    public ChainedBritishTimeFormatter() {
        this.formatterChain = buildDefaultChain();
    }

    /**
     * Creates a new ChainedBritishTimeFormatter with a custom formatter chain.
     * 
     * @param formatterChain the root of the formatter chain
     */
    public ChainedBritishTimeFormatter(AbstractTimeFormatter formatterChain) {
        this.formatterChain = Objects.requireNonNull(formatterChain, "formatterChain must not be null");
    }

    @Override
    public String format(LocalTime time) {
        Objects.requireNonNull(time, "time must not be null");
        return formatterChain.format(time);
    }

    /**
     * Builds the default chain of formatters in the correct order.
     * 
     * @return the root formatter of the chain
     */
    private AbstractTimeFormatter buildDefaultChain() {
        AbstractTimeFormatter midnight = new MidnightFormatter();
        AbstractTimeFormatter noon = new NoonFormatter();
        AbstractTimeFormatter oClock = new OClockFormatter();
        AbstractTimeFormatter quarterPast = new QuarterPastFormatter();
        AbstractTimeFormatter halfPast = new HalfPastFormatter();
        AbstractTimeFormatter thirtyPlus = new ThirtyPlusFormatter();
        AbstractTimeFormatter quarterTo = new QuarterToFormatter();
        AbstractTimeFormatter minutesPast = new MinutesPastFormatter();
        AbstractTimeFormatter minutesTo = new MinutesToFormatter();

        // Chain them together
        midnight.setNext(noon)
                .setNext(oClock)
                .setNext(quarterPast)
                .setNext(halfPast)
                .setNext(thirtyPlus)
                .setNext(quarterTo)
                .setNext(minutesPast)
                .setNext(minutesTo);

        return midnight;
    }

    /**
     * Builder class for creating custom formatter chains.
     */
    public static class Builder {
        private AbstractTimeFormatter head;
        private AbstractTimeFormatter tail;

        /**
         * Adds a formatter to the chain.
         * 
         * @param formatter the formatter to add
         * @return this builder for method chaining
         */
        public Builder addFormatter(AbstractTimeFormatter formatter) {
            if (head == null) {
                head = formatter;
                tail = formatter;
            } else {
                tail.setNext(formatter);
                tail = formatter;
            }
            return this;
        }

        /**
         * Builds the ChainedBritishTimeFormatter with the configured chain.
         * 
         * @return a new ChainedBritishTimeFormatter instance
         */
        public ChainedBritishTimeFormatter build() {
            if (head == null) {
                throw new IllegalStateException("At least one formatter must be added to the chain");
            }
            return new ChainedBritishTimeFormatter(head);
        }
    }
}
