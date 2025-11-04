package com.kamlesh.britishtime.formatter;

import com.kamlesh.britishtime.service.formatter.*;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for individual formatter implementations.
 * Tests each formatter in isolation to ensure it only handles its specific cases.
 */
class IndividualFormatterTest {

    @Test
    void testMidnightFormatter() {
        MidnightFormatter formatter = new MidnightFormatter();
        
        // Should handle midnight
        assertEquals(Optional.of("midnight"), formatter.tryFormat(LocalTime.of(0, 0)));
        
        // Should not handle other times
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(0, 1)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(12, 0)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 0)));
    }

    @Test
    void testNoonFormatter() {
        NoonFormatter formatter = new NoonFormatter();
        
        // Should handle noon
        assertEquals(Optional.of("noon"), formatter.tryFormat(LocalTime.of(12, 0)));
        
        // Should not handle other times
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(0, 0)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(12, 1)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(13, 0)));
    }

    @Test
    void testOClockFormatter() {
        OClockFormatter formatter = new OClockFormatter();
        
        // Should handle exact hours
        assertEquals(Optional.of("three o'clock"), formatter.tryFormat(LocalTime.of(3, 0)));
        assertEquals(Optional.of("nine o'clock"), formatter.tryFormat(LocalTime.of(9, 0)));
        assertEquals(Optional.of("three o'clock"), formatter.tryFormat(LocalTime.of(15, 0)));
        
        // Should not handle non-zero minutes
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 15)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 30)));
    }

    @Test
    void testQuarterPastFormatter() {
        QuarterPastFormatter formatter = new QuarterPastFormatter();
        
        // Should handle 15 minutes
        assertEquals(Optional.of("quarter past three"), formatter.tryFormat(LocalTime.of(3, 15)));
        assertEquals(Optional.of("quarter past nine"), formatter.tryFormat(LocalTime.of(9, 15)));
        
        // Should not handle other minutes
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 0)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 30)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 45)));
    }

    @Test
    void testHalfPastFormatter() {
        HalfPastFormatter formatter = new HalfPastFormatter();
        
        // Should handle 30 minutes
        assertEquals(Optional.of("half past three"), formatter.tryFormat(LocalTime.of(3, 30)));
        assertEquals(Optional.of("half past nine"), formatter.tryFormat(LocalTime.of(9, 30)));
        
        // Should not handle other minutes
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 0)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 15)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 45)));
    }

    @Test
    void testQuarterToFormatter() {
        QuarterToFormatter formatter = new QuarterToFormatter();
        
        // Should handle 45 minutes
        assertEquals(Optional.of("quarter to four"), formatter.tryFormat(LocalTime.of(3, 45)));
        assertEquals(Optional.of("quarter to ten"), formatter.tryFormat(LocalTime.of(9, 45)));
        
        // Should not handle other minutes
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 0)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 15)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 30)));
    }

    @Test
    void testMinutesPastFormatter() {
        MinutesPastFormatter formatter = new MinutesPastFormatter();
        
        // Should handle 1-30 minutes (excluding 15 and 30)
        assertEquals(Optional.of("five past three"), formatter.tryFormat(LocalTime.of(3, 5)));
        assertEquals(Optional.of("ten past three"), formatter.tryFormat(LocalTime.of(3, 10)));
        assertEquals(Optional.of("twenty past three"), formatter.tryFormat(LocalTime.of(3, 20)));
        assertEquals(Optional.of("twenty five past three"), formatter.tryFormat(LocalTime.of(3, 25)));
        
        // Should not handle special cases
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 0)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 15)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 30)));
        
        // Should not handle minutes > 30
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 35)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 45)));
    }

    @Test
    void testMinutesToFormatter() {
        MinutesToFormatter formatter = new MinutesToFormatter();
        
        // Should handle 31-34 minutes (special case)
        assertEquals(Optional.of("three thirty one"), formatter.tryFormat(LocalTime.of(3, 31)));
        assertEquals(Optional.of("three thirty two"), formatter.tryFormat(LocalTime.of(3, 32)));
        
        // Should handle 35-59 minutes (excluding 45)
        assertEquals(Optional.of("twenty five to four"), formatter.tryFormat(LocalTime.of(3, 35)));
        assertEquals(Optional.of("twenty to four"), formatter.tryFormat(LocalTime.of(3, 40)));
        assertEquals(Optional.of("ten to four"), formatter.tryFormat(LocalTime.of(3, 50)));
        assertEquals(Optional.of("five to four"), formatter.tryFormat(LocalTime.of(3, 55)));
        
        // Should not handle minutes <= 30
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 0)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 15)));
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 30)));
        
        // Should not handle 45 minutes
        assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(3, 45)));
    }
}
