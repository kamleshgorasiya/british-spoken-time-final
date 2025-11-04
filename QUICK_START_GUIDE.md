# Quick Start Guide: Adding Custom Time Formatters

## 5-Minute Tutorial

### Scenario
You want to add a formatter that converts 5:00 AM to "dawn".

### Step 1: Create Your Formatter (2 minutes)

Create a new file: `DawnFormatter.java`

```java
package com.kamlesh.britishtime.service.formatter;

import java.time.LocalTime;
import java.util.Optional;

public class DawnFormatter extends AbstractTimeFormatter {
    
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        if (time.getHour() == 5 && time.getMinute() == 0) {
            return Optional.of("dawn");
        }
        return Optional.empty();
    }
}
```

**That's it!** Your formatter is complete.

### Step 2: Add to Chain (1 minute)

Update `FormatterConfiguration.java`:

```java
@Bean
public TimeSpokenFormatter timeSpokenFormatter() {
    return new ChainedBritishTimeFormatter.Builder()
        .addFormatter(new MidnightFormatter())
        .addFormatter(new NoonFormatter())
        .addFormatter(new DawnFormatter())        // ← Add this line
        .addFormatter(new OClockFormatter())
        .addFormatter(new QuarterPastFormatter())
        .addFormatter(new HalfPastFormatter())
        .addFormatter(new QuarterToFormatter())
        .addFormatter(new MinutesPastFormatter())
        .addFormatter(new MinutesToFormatter())
        .build();
}
```

### Step 3: Test It (2 minutes)

Create a test: `DawnFormatterTest.java`

```java
@Test
void testDawnFormatter() {
    DawnFormatter formatter = new DawnFormatter();
    
    // Should handle 5:00 AM
    assertEquals(Optional.of("dawn"), 
                 formatter.tryFormat(LocalTime.of(5, 0)));
    
    // Should not handle other times
    assertEquals(Optional.empty(), 
                 formatter.tryFormat(LocalTime.of(6, 0)));
}
```

### Done! 🎉

Now when you call:
```java
formatter.format(LocalTime.of(5, 0));
```

You get: `"dawn"`

---

## Common Patterns

### Pattern 1: Exact Time Match
```java
public class MidnightFormatter extends AbstractTimeFormatter {
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        if (time.getHour() == 0 && time.getMinute() == 0) {
            return Optional.of("midnight");
        }
        return Optional.empty();
    }
}
```

### Pattern 2: Minute Match (Any Hour)
```java
public class QuarterPastFormatter extends AbstractTimeFormatter {
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        if (time.getMinute() == 15) {
            int hour12 = time.getHour() % 12;
            return Optional.of("quarter past " + TimeWords.hourWord(hour12));
        }
        return Optional.empty();
    }
}
```

### Pattern 3: Range Match
```java
public class MinutesPastFormatter extends AbstractTimeFormatter {
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        int minute = time.getMinute();
        
        if (minute > 0 && minute <= 30 && minute != 15 && minute != 30) {
            int hour12 = time.getHour() % 12;
            String minuteSpoken = minuteToSpoken(minute);
            return Optional.of(minuteSpoken + " past " + TimeWords.hourWord(hour12));
        }
        
        return Optional.empty();
    }
    
    private String minuteToSpoken(int minute) {
        if (minute < 20) return TimeWords.unitWord(minute);
        int tens = minute / 10;
        int ones = minute % 10;
        String tensWord = TimeWords.tensWord(tens);
        return ones == 0 ? tensWord : tensWord + " " + TimeWords.unitWord(ones);
    }
}
```

### Pattern 4: Time Period Match
```java
public class MorningFormatter extends AbstractTimeFormatter {
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        int hour = time.getHour();
        
        if (hour >= 6 && hour < 12 && time.getMinute() == 0) {
            int hour12 = hour % 12;
            return Optional.of(TimeWords.hourWord(hour12) + " in the morning");
        }
        
        return Optional.empty();
    }
}
```

---

## Real-World Examples

### Example 1: Add "Dusk" (6:00 PM)

```java
public class DuskFormatter extends AbstractTimeFormatter {
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        if (time.getHour() == 18 && time.getMinute() == 0) {
            return Optional.of("dusk");
        }
        return Optional.empty();
    }
}
```

### Example 2: Add "Approximate Time" (within 2 minutes)

```java
public class ApproximateTimeFormatter extends AbstractTimeFormatter {
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        int hour12 = time.getHour() % 12;
        int minute = time.getMinute();
        
        // 2:58, 2:59, 3:01, 3:02 → "about three o'clock"
        if (minute >= 58 || minute <= 2) {
            int displayHour = minute >= 58 ? (hour12 + 1) % 12 : hour12;
            return Optional.of("about " + TimeWords.hourWord(displayHour) + " o'clock");
        }
        
        return Optional.empty();
    }
}
```

### Example 3: Add Regional Variant (Scottish)

```java
public class ScottishHalfFormatter extends AbstractTimeFormatter {
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        if (time.getMinute() == 30) {
            int hour12 = time.getHour() % 12;
            int nextHour = (hour12 + 1) % 12;
            // Scottish: "half three" means 2:30 (half to three)
            return Optional.of("half " + TimeWords.hourWord(nextHour));
        }
        return Optional.empty();
    }
}
```

---

## Tips & Best Practices

### ✅ DO

1. **Keep formatters simple** - One condition, one responsibility
2. **Return Optional.empty()** - When your formatter can't handle the time
3. **Use TimeWords utility** - For consistent word mappings
4. **Write tests first** - TDD makes it easier
5. **Order matters** - More specific formatters should come first in the chain

### ❌ DON'T

1. **Don't modify existing formatters** - Create a new one instead
2. **Don't handle multiple cases** - Split into separate formatters
3. **Don't forget to test** - Both positive and negative cases
4. **Don't hardcode strings** - Use constants or configuration
5. **Don't break the chain** - Always return Optional, never null

---

## Troubleshooting

### Problem: My formatter is never called

**Solution**: Check the chain order. A formatter earlier in the chain might be handling your case.

```java
// Wrong order - OClockFormatter handles 5:00 before DawnFormatter
.addFormatter(new OClockFormatter())
.addFormatter(new DawnFormatter())

// Correct order - DawnFormatter gets first chance
.addFormatter(new DawnFormatter())
.addFormatter(new OClockFormatter())
```

### Problem: Multiple formatters handle the same time

**Solution**: Make conditions more specific or adjust chain order.

```java
// Both handle 3:00
public class OClockFormatter {
    if (time.getMinute() == 0) { ... }
}

public class MorningFormatter {
    if (hour >= 6 && hour < 12 && minute == 0) { ... }
}

// Solution: Make MorningFormatter more specific
public class MorningFormatter {
    if (hour >= 6 && hour < 12 && minute == 0 && !isSpecialTime(time)) { ... }
}
```

### Problem: Tests pass individually but fail in integration

**Solution**: Check for side effects or shared state. Formatters should be stateless.

```java
// Bad - shared state
public class BadFormatter extends AbstractTimeFormatter {
    private int counter = 0;  // ❌ Don't do this
    
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        counter++;  // ❌ Side effect
        ...
    }
}

// Good - stateless
public class GoodFormatter extends AbstractTimeFormatter {
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        // No state, no side effects ✅
        if (time.getHour() == 5) {
            return Optional.of("dawn");
        }
        return Optional.empty();
    }
}
```

---

## Testing Checklist

When you create a new formatter, test:

- ✅ It handles its specific case correctly
- ✅ It returns empty for cases it shouldn't handle
- ✅ It works with edge cases (midnight, noon, etc.)
- ✅ It integrates correctly with the full chain
- ✅ It doesn't break existing tests

### Example Test Template

```java
@Test
void testMyFormatter() {
    MyFormatter formatter = new MyFormatter();
    
    // Test positive cases
    assertEquals(Optional.of("expected"), 
                 formatter.tryFormat(LocalTime.of(X, Y)));
    
    // Test negative cases
    assertEquals(Optional.empty(), 
                 formatter.tryFormat(LocalTime.of(A, B)));
    
    // Test edge cases
    assertEquals(Optional.empty(), 
                 formatter.tryFormat(LocalTime.of(0, 0)));
}
```

---

## Next Steps

1. **Read**: `FORMATTER_ARCHITECTURE.md` for complete documentation
2. **Explore**: `CustomFormatterExample.java` for more examples
3. **Study**: Existing formatters in the codebase
4. **Experiment**: Create your own custom formatter
5. **Share**: Contribute your formatters back to the project

---

## Quick Reference

### File Locations
```
src/main/java/com/kamlesh/britishtime/service/formatter/
├── AbstractTimeFormatter.java       ← Extend this
├── ChainedBritishTimeFormatter.java ← Use Builder here
└── YourFormatter.java               ← Create here

src/test/java/com/kamlesh/britishtime/formatter/
└── YourFormatterTest.java           ← Test here
```

### Key Classes
- **AbstractTimeFormatter** - Base class for all formatters
- **ChainedBritishTimeFormatter** - Chains formatters together
- **TimeWords** - Utility for word mappings
- **TimeSpokenFormatter** - Interface all formatters implement

### Useful Methods
- `TimeWords.hourWord(int)` - Get hour word (0-12)
- `TimeWords.unitWord(int)` - Get number word (0-19)
- `TimeWords.tensWord(int)` - Get tens word (20, 30, 40, 50)

---

## Help & Support

- **Documentation**: See `FORMATTER_ARCHITECTURE.md`
- **Examples**: See `CustomFormatterExample.java`
- **Tests**: See `IndividualFormatterTest.java`
- **Diagrams**: See `ARCHITECTURE_DIAGRAM.md`

Happy coding! 🚀
