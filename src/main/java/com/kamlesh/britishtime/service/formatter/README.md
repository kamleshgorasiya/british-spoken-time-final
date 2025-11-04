# Time Formatter Architecture

## Overview
This package implements the **Chain of Responsibility** design pattern for formatting time into British spoken English. Each formatter handles specific time conditions and delegates to the next formatter if it cannot handle the input.

## Architecture

### Core Components

1. **TimeSpokenFormatter** (Interface)
   - Base interface for all time formatters
   - Single method: `String format(LocalTime time)`

2. **AbstractTimeFormatter** (Abstract Base Class)
   - Implements the chain of responsibility logic
   - Provides `tryFormat()` method for subclasses to implement
   - Handles delegation to the next formatter in the chain

3. **Specialized Formatters**
   - `MidnightFormatter` - Handles 00:00 → "midnight"
   - `NoonFormatter` - Handles 12:00 → "noon"
   - `OClockFormatter` - Handles exact hours → "three o'clock"
   - `QuarterPastFormatter` - Handles 15 minutes → "quarter past three"
   - `HalfPastFormatter` - Handles 30 minutes → "half past three"
   - `QuarterToFormatter` - Handles 45 minutes → "quarter to four"
   - `MinutesPastFormatter` - Handles 1-30 minutes → "five past three"
   - `MinutesToFormatter` - Handles 31-59 minutes → "five to four"

4. **ChainedBritishTimeFormatter** (Composite)
   - Chains all specialized formatters together
   - Provides default chain configuration
   - Includes a Builder for custom chains

## Usage

### Default Usage (Recommended)
```java
TimeSpokenFormatter formatter = new ChainedBritishTimeFormatter();
String result = formatter.format(LocalTime.of(3, 15));
// Output: "quarter past three"
```

### Custom Chain with Builder
```java
TimeSpokenFormatter formatter = new ChainedBritishTimeFormatter.Builder()
    .addFormatter(new MidnightFormatter())
    .addFormatter(new NoonFormatter())
    .addFormatter(new OClockFormatter())
    .addFormatter(new MinutesPastFormatter())
    .addFormatter(new MinutesToFormatter())
    .build();
```

### Spring Configuration
The formatter is configured as a Spring bean in `FormatterConfiguration`:
```java
@Bean
public TimeSpokenFormatter timeSpokenFormatter() {
    return new ChainedBritishTimeFormatter();
}
```

## Adding New Formatters

To add a new formatter for specific time conditions:

1. **Create a new formatter class** extending `AbstractTimeFormatter`:
```java
public class MyCustomFormatter extends AbstractTimeFormatter {
    
    @Override
    protected Optional<String> tryFormat(LocalTime time) {
        // Check if this formatter can handle the time
        if (/* your condition */) {
            return Optional.of("your formatted string");
        }
        return Optional.empty();
    }
}
```

2. **Add it to the chain** in `ChainedBritishTimeFormatter` or use the Builder:
```java
private AbstractTimeFormatter buildDefaultChain() {
    // ... existing formatters
    AbstractTimeFormatter myCustom = new MyCustomFormatter();
    
    midnight.setNext(noon)
            .setNext(myCustom)  // Add your formatter
            .setNext(oClock)
            // ... rest of chain
}
```

## Chain Order Matters

The order of formatters in the chain is important:
1. More specific conditions should come first
2. General/fallback formatters should come last
3. Example: `MidnightFormatter` before `OClockFormatter` (both handle minute=0)

## Benefits of This Architecture

1. **Single Responsibility** - Each formatter handles one specific case
2. **Open/Closed Principle** - Easy to add new formatters without modifying existing ones
3. **Testability** - Each formatter can be tested independently
4. **Maintainability** - Clear separation of concerns
5. **Flexibility** - Easy to reorder or customize the chain
6. **Extensibility** - New time formats can be added without changing existing code

## Example Formatters You Could Add

- `DawnFormatter` - Handle 5:00-6:00 as "dawn"
- `DuskFormatter` - Handle 18:00-19:00 as "dusk"
- `MilitaryTimeFormatter` - Handle 24-hour format
- `ApproximateTimeFormatter` - Handle "about three o'clock" for 2:58-3:02
- `RegionalVariantFormatter` - Handle regional British variations

## Testing

Each formatter should have unit tests:
```java
@Test
void testMidnightFormatter() {
    MidnightFormatter formatter = new MidnightFormatter();
    Optional<String> result = formatter.tryFormat(LocalTime.of(0, 0));
    assertEquals("midnight", result.get());
}
```
