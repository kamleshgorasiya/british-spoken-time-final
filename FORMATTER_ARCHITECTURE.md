# British Spoken Time Formatter - Architecture Documentation

## Overview
This document describes the refactored architecture for the British Spoken Time formatter, which now uses the **Chain of Responsibility** design pattern to separate concerns and make the code more maintainable and extensible.

## Problem Statement
The original `BritishTimeFormatter` class contained all formatting logic in a single method with multiple conditional statements. This made it:
- Hard to maintain
- Difficult to test individual cases
- Challenging to add new formatting rules
- Violating Single Responsibility Principle

## Solution: Chain of Responsibility Pattern

### Architecture Diagram
```
TimeSpokenFormatter (Interface)
        ↑
        |
AbstractTimeFormatter (Abstract Class)
        ↑
        |
        +-- MidnightFormatter
        +-- NoonFormatter
        +-- OClockFormatter
        +-- QuarterPastFormatter
        +-- HalfPastFormatter
        +-- QuarterToFormatter
        +-- MinutesPastFormatter
        +-- MinutesToFormatter
        
ChainedBritishTimeFormatter (Composite)
    - Chains all formatters together
    - Provides Builder for custom chains
```

### Component Responsibilities

#### 1. TimeSpokenFormatter (Interface)
```java
public interface TimeSpokenFormatter {
    String format(LocalTime time);
}
```
- Base contract for all formatters
- Single method for formatting time

#### 2. AbstractTimeFormatter (Abstract Base Class)
```java
public abstract class AbstractTimeFormatter implements TimeSpokenFormatter {
    protected AbstractTimeFormatter nextFormatter;
    
    public AbstractTimeFormatter setNext(AbstractTimeFormatter nextFormatter);
    public String format(LocalTime time);
    protected abstract Optional<String> tryFormat(LocalTime time);
}
```
- Implements Chain of Responsibility logic
- Delegates to next formatter if current cannot handle
- Subclasses implement `tryFormat()` for specific cases

#### 3. Specialized Formatters
Each formatter handles ONE specific case:

| Formatter | Handles | Example |
|-----------|---------|---------|
| MidnightFormatter | 00:00 | "midnight" |
| NoonFormatter | 12:00 | "noon" |
| OClockFormatter | XX:00 | "three o'clock" |
| QuarterPastFormatter | XX:15 | "quarter past three" |
| HalfPastFormatter | XX:30 | "half past three" |
| QuarterToFormatter | XX:45 | "quarter to four" |
| MinutesPastFormatter | XX:01-30 | "five past three" |
| MinutesToFormatter | XX:31-59 | "five to four" |

#### 4. ChainedBritishTimeFormatter (Composite)
```java
public class ChainedBritishTimeFormatter implements TimeSpokenFormatter {
    private final AbstractTimeFormatter formatterChain;
    
    public ChainedBritishTimeFormatter();  // Default chain
    public ChainedBritishTimeFormatter(AbstractTimeFormatter formatterChain);  // Custom chain
    
    public static class Builder { ... }  // Builder for custom chains
}
```
- Chains all formatters in the correct order
- Provides default configuration
- Includes Builder for custom chains

## Usage Examples

### Basic Usage
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
    .addFormatter(new CustomFormatter())  // Your custom formatter
    .addFormatter(new OClockFormatter())
    .build();
```

### Spring Configuration
```java
@Configuration
public class FormatterConfiguration {
    @Bean
    public TimeSpokenFormatter timeSpokenFormatter() {
        return new ChainedBritishTimeFormatter();
    }
}
```

## Adding New Formatters

### Step 1: Create Formatter Class
```java
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

### Step 2: Add to Chain
```java
// Option 1: Modify ChainedBritishTimeFormatter
private AbstractTimeFormatter buildDefaultChain() {
    midnight.setNext(noon)
            .setNext(new DawnFormatter())  // Add here
            .setNext(oClock)
            // ... rest of chain
}

// Option 2: Use Builder
TimeSpokenFormatter formatter = new ChainedBritishTimeFormatter.Builder()
    .addFormatter(new MidnightFormatter())
    .addFormatter(new DawnFormatter())  // Add here
    .addFormatter(new NoonFormatter())
    .build();
```

### Step 3: Write Tests
```java
@Test
void testDawnFormatter() {
    DawnFormatter formatter = new DawnFormatter();
    assertEquals(Optional.of("dawn"), formatter.tryFormat(LocalTime.of(5, 0)));
    assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(6, 0)));
}
```

## Benefits of This Architecture

### 1. Single Responsibility Principle ✅
Each formatter has ONE job - handle a specific time condition.

### 2. Open/Closed Principle ✅
Open for extension (add new formatters), closed for modification (existing formatters don't change).

### 3. Testability ✅
Each formatter can be tested independently with focused unit tests.

### 4. Maintainability ✅
Clear separation of concerns makes code easier to understand and maintain.

### 5. Flexibility ✅
Easy to reorder formatters or create custom chains for different use cases.

### 6. Extensibility ✅
New time formats can be added without modifying existing code.

## Design Patterns Used

### 1. Chain of Responsibility
- Each formatter tries to handle the request
- If it can't, it delegates to the next formatter
- Decouples sender from receiver

### 2. Strategy Pattern
- `TimeSpokenFormatter` interface defines the strategy
- Different implementations provide different algorithms
- Client can switch strategies at runtime

### 3. Builder Pattern
- `ChainedBritishTimeFormatter.Builder` provides fluent API
- Simplifies creation of complex formatter chains
- Improves readability

### 4. Template Method Pattern
- `AbstractTimeFormatter.format()` defines the algorithm structure
- Subclasses implement `tryFormat()` for specific behavior
- Promotes code reuse

## File Structure
```
src/main/java/com/kamlesh/britishtime/service/formatter/
├── TimeSpokenFormatter.java              (Interface)
├── AbstractTimeFormatter.java            (Abstract Base)
├── ChainedBritishTimeFormatter.java      (Composite)
├── MidnightFormatter.java                (Concrete)
├── NoonFormatter.java                    (Concrete)
├── OClockFormatter.java                  (Concrete)
├── QuarterPastFormatter.java             (Concrete)
├── HalfPastFormatter.java                (Concrete)
├── QuarterToFormatter.java               (Concrete)
├── MinutesPastFormatter.java             (Concrete)
├── MinutesToFormatter.java               (Concrete)
├── TimeWords.java                        (Utility)
├── BritishTimeFormatter.java             (Legacy - can be deprecated)
├── README.md                             (Documentation)
└── examples/
    └── CustomFormatterExample.java       (Examples)

src/test/java/com/kamlesh/britishtime/formatter/
├── ChainedBritishTimeFormatterTest.java  (Integration tests)
└── IndividualFormatterTest.java          (Unit tests)
```

## Migration Path

### For Existing Code
The old `BritishTimeFormatter` can remain for backward compatibility, but new code should use `ChainedBritishTimeFormatter`.

### Deprecation Strategy
1. Mark `BritishTimeFormatter` as `@Deprecated`
2. Update Spring configuration to use `ChainedBritishTimeFormatter`
3. Update all references in codebase
4. Remove deprecated class in next major version

## Performance Considerations

### Time Complexity
- Best case: O(1) - First formatter handles it
- Worst case: O(n) - Last formatter handles it (where n = number of formatters)
- Average case: O(n/2) - Middle formatter handles it

### Space Complexity
- O(n) - Chain of n formatters

### Optimization Tips
1. Order formatters by frequency (most common cases first)
2. Keep chain length reasonable (8-12 formatters is fine)
3. Consider caching for repeated time values (if needed)

## Testing Strategy

### Unit Tests
Test each formatter in isolation:
```java
@Test
void testMidnightFormatter() {
    MidnightFormatter formatter = new MidnightFormatter();
    assertEquals(Optional.of("midnight"), formatter.tryFormat(LocalTime.of(0, 0)));
    assertEquals(Optional.empty(), formatter.tryFormat(LocalTime.of(1, 0)));
}
```

### Integration Tests
Test the complete chain:
```java
@ParameterizedTest
@CsvSource({
    "00:00, midnight",
    "03:15, quarter past three",
    "03:45, quarter to four"
})
void testCompleteChain(String timeStr, String expected) {
    TimeSpokenFormatter formatter = new ChainedBritishTimeFormatter();
    assertEquals(expected, formatter.format(LocalTime.parse(timeStr)));
}
```

## Future Enhancements

### Potential New Formatters
1. **DawnFormatter** - Handle 5:00 AM as "dawn"
2. **DuskFormatter** - Handle 6:00 PM as "dusk"
3. **ApproximateTimeFormatter** - Handle "about three o'clock" for 2:58-3:02
4. **TimePeriodFormatter** - Add "in the morning/afternoon/evening"
5. **RegionalVariantFormatter** - Handle regional British variations
6. **MilitaryTimeFormatter** - Handle 24-hour format

### Internationalization
The architecture supports multiple locales:
```java
@Bean
public TimeSpokenFormatter britishFormatter() {
    return new ChainedBritishTimeFormatter();
}

@Bean
public TimeSpokenFormatter americanFormatter() {
    return new ChainedAmericanTimeFormatter();  // Future implementation
}
```

## Conclusion

The refactored architecture provides a clean, maintainable, and extensible solution for time formatting. The Chain of Responsibility pattern allows for easy addition of new formatters without modifying existing code, making the system more robust and easier to test.

## References
- [Chain of Responsibility Pattern](https://refactoring.guru/design-patterns/chain-of-responsibility)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Clean Code by Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
