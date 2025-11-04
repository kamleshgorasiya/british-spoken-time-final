# Architecture Diagram: British Spoken Time Formatter

## System Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                     Spring Boot Application                      │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      TimeController                              │
│  @GetMapping("/api/time/spoken")                                │
│  public ResponseEntity<SpokenTimeResponse> spoken(String time)  │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      TimeServiceImpl                             │
│  public SpokenTimeResponse toSpokenTime(String timeStr)         │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                 TimeSpokenFormatter (Interface)                  │
│  String format(LocalTime time)                                  │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChainedBritishTimeFormatter (Composite)             │
│  - Contains chain of specialized formatters                     │
│  - Delegates to first formatter in chain                        │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │  Formatter Chain      │
                    └───────────────────────┘
```

## Formatter Chain Flow

```
Input: LocalTime
     │
     ▼
┌─────────────────────────┐
│  MidnightFormatter      │  Can handle? → YES → Return "midnight"
│  (00:00)                │              ↓ NO
└─────────────────────────┘              │
                                         ▼
┌─────────────────────────┐
│  NoonFormatter          │  Can handle? → YES → Return "noon"
│  (12:00)                │              ↓ NO
└─────────────────────────┘              │
                                         ▼
┌─────────────────────────┐
│  OClockFormatter        │  Can handle? → YES → Return "X o'clock"
│  (XX:00)                │              ↓ NO
└─────────────────────────┘              │
                                         ▼
┌─────────────────────────┐
│  QuarterPastFormatter   │  Can handle? → YES → Return "quarter past X"
│  (XX:15)                │              ↓ NO
└─────────────────────────┘              │
                                         ▼
┌─────────────────────────┐
│  HalfPastFormatter      │  Can handle? → YES → Return "half past X"
│  (XX:30)                │              ↓ NO
└─────────────────────────┘              │
                                         ▼
┌─────────────────────────┐
│  QuarterToFormatter     │  Can handle? → YES → Return "quarter to X"
│  (XX:45)                │              ↓ NO
└─────────────────────────┘              │
                                         ▼
┌─────────────────────────┐
│  MinutesPastFormatter   │  Can handle? → YES → Return "Y past X"
│  (XX:01-30)             │              ↓ NO
└─────────────────────────┘              │
                                         ▼
┌─────────────────────────┐
│  MinutesToFormatter     │  Can handle? → YES → Return "Y to X"
│  (XX:31-59)             │              ↓ NO
└─────────────────────────┘              │
                                         ▼
                              No formatter handled it
                              → Throw Exception
```

## Class Diagram

```
┌──────────────────────────────────────┐
│   <<interface>>                      │
│   TimeSpokenFormatter                │
├──────────────────────────────────────┤
│ + format(LocalTime): String          │
└──────────────────────────────────────┘
            △                    △
            │                    │
            │                    │
┌───────────┴──────────┐    ┌────┴──────────────────────┐
│                      │    │                           │
│ BritishTimeFormatter │    │ AbstractTimeFormatter     │
│ (Legacy)             │    │ (Abstract)                │
├──────────────────────┤    ├───────────────────────────┤
│ + format()           │    │ # nextFormatter           │
└──────────────────────┘    │ + setNext()               │
                            │ + format()                │
                            │ # tryFormat(): Optional   │
                            └───────────────────────────┘
                                        △
                                        │
                    ┌───────────────────┼───────────────────┐
                    │                   │                   │
        ┌───────────┴────────┐  ┌──────┴────────┐  ┌──────┴────────┐
        │ MidnightFormatter  │  │ NoonFormatter │  │ OClockFormatter│
        ├────────────────────┤  ├───────────────┤  ├────────────────┤
        │ # tryFormat()      │  │ # tryFormat() │  │ # tryFormat()  │
        └────────────────────┘  └───────────────┘  └────────────────┘
                    
        ┌────────────────────┐  ┌───────────────────┐  ┌──────────────────┐
        │QuarterPastFormatter│  │ HalfPastFormatter │  │QuarterToFormatter│
        ├────────────────────┤  ├───────────────────┤  ├──────────────────┤
        │ # tryFormat()      │  │ # tryFormat()     │  │ # tryFormat()    │
        └────────────────────┘  └───────────────────┘  └──────────────────┘

        ┌────────────────────┐  ┌───────────────────┐
        │MinutesPastFormatter│  │ MinutesToFormatter│
        ├────────────────────┤  ├───────────────────┤
        │ # tryFormat()      │  │ # tryFormat()     │
        └────────────────────┘  └───────────────────┘


┌──────────────────────────────────────────────────────┐
│   ChainedBritishTimeFormatter                        │
│   (Composite)                                        │
├──────────────────────────────────────────────────────┤
│ - formatterChain: AbstractTimeFormatter              │
├──────────────────────────────────────────────────────┤
│ + ChainedBritishTimeFormatter()                      │
│ + ChainedBritishTimeFormatter(chain)                 │
│ + format(LocalTime): String                          │
│ - buildDefaultChain(): AbstractTimeFormatter         │
├──────────────────────────────────────────────────────┤
│   <<nested class>>                                   │
│   Builder                                            │
│   ├────────────────────────────────────────────────┤ │
│   │ + addFormatter(formatter): Builder             │ │
│   │ + build(): ChainedBritishTimeFormatter         │ │
│   └────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────┘
```

## Sequence Diagram: Processing a Time

```
Client          ChainedFormatter    MidnightFormatter    NoonFormatter    OClockFormatter
  │                    │                    │                  │                │
  │ format(03:00)      │                    │                  │                │
  ├───────────────────>│                    │                  │                │
  │                    │                    │                  │                │
  │                    │ tryFormat(03:00)   │                  │                │
  │                    ├───────────────────>│                  │                │
  │                    │                    │                  │                │
  │                    │ Optional.empty()   │                  │                │
  │                    │<───────────────────┤                  │                │
  │                    │                    │                  │                │
  │                    │      tryFormat(03:00)                 │                │
  │                    ├──────────────────────────────────────>│                │
  │                    │                    │                  │                │
  │                    │      Optional.empty()                 │                │
  │                    │<──────────────────────────────────────┤                │
  │                    │                    │                  │                │
  │                    │           tryFormat(03:00)            │                │
  │                    ├───────────────────────────────────────────────────────>│
  │                    │                    │                  │                │
  │                    │      Optional.of("three o'clock")     │                │
  │                    │<───────────────────────────────────────────────────────┤
  │                    │                    │                  │                │
  │ "three o'clock"    │                    │                  │                │
  │<───────────────────┤                    │                  │                │
  │                    │                    │                  │                │
```

## Component Interaction

```
┌─────────────────────────────────────────────────────────────────┐
│                    Spring Configuration                          │
│                                                                  │
│  @Configuration                                                  │
│  public class FormatterConfiguration {                          │
│                                                                  │
│      @Bean                                                       │
│      public TimeSpokenFormatter timeSpokenFormatter() {         │
│          return new ChainedBritishTimeFormatter();              │
│      }                                                           │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                            │
                            │ Creates & Injects
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    TimeServiceImpl                               │
│                                                                  │
│  @Service                                                        │
│  public class TimeServiceImpl {                                 │
│      private final TimeSpokenFormatter formatter;               │
│                                                                  │
│      public SpokenTimeResponse toSpokenTime(String timeStr) {   │
│          LocalTime time = TimeParser.parse(timeStr);            │
│          String spoken = formatter.format(time);                │
│          return new SpokenTimeResponse(timeStr, spoken);        │
│      }                                                           │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                            │
                            │ Uses
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│              ChainedBritishTimeFormatter                         │
│                                                                  │
│  Internally manages:                                             │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Midnight → Noon → OClock → QuarterPast → HalfPast →       │ │
│  │ QuarterTo → MinutesPast → MinutesTo                        │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## Data Flow

```
HTTP Request
     │
     │ GET /api/time/spoken?time=03:15
     ▼
┌─────────────────┐
│ TimeController  │
└─────────────────┘
     │
     │ String "03:15"
     ▼
┌─────────────────┐
│ TimeServiceImpl │
└─────────────────┘
     │
     │ Parse to LocalTime(3, 15)
     ▼
┌──────────────────────────┐
│ ChainedBritishFormatter  │
└──────────────────────────┘
     │
     │ LocalTime(3, 15)
     ▼
┌──────────────────────────┐
│ Formatter Chain          │
│ (tries each formatter)   │
└──────────────────────────┘
     │
     │ "quarter past three"
     ▼
┌─────────────────┐
│ TimeServiceImpl │
└─────────────────┘
     │
     │ SpokenTimeResponse("03:15", "quarter past three")
     ▼
┌─────────────────┐
│ TimeController  │
└─────────────────┘
     │
     │ JSON Response
     ▼
HTTP Response
{
  "input": "03:15",
  "spoken": "quarter past three"
}
```

## Extension Point: Adding Custom Formatter

```
Step 1: Create Formatter
┌─────────────────────────┐
│  DawnFormatter          │
│  extends                │
│  AbstractTimeFormatter  │
├─────────────────────────┤
│ # tryFormat()           │
│   if (hour == 5 &&      │
│       minute == 0)      │
│     return "dawn"       │
└─────────────────────────┘

Step 2: Add to Chain
┌──────────────────────────────────────┐
│ ChainedBritishTimeFormatter.Builder  │
├──────────────────────────────────────┤
│ .addFormatter(new MidnightFormatter)│
│ .addFormatter(new DawnFormatter)    │ ← Add here
│ .addFormatter(new NoonFormatter)    │
│ .build()                            │
└──────────────────────────────────────┘

Step 3: Test
┌─────────────────────────────────────┐
│ @Test                               │
│ void testDawn() {                   │
│   formatter.format(                 │
│     LocalTime.of(5, 0)              │
│   );                                │
│   // Returns "dawn"                 │
│ }                                   │
└─────────────────────────────────────┘
```

## Design Pattern Relationships

```
┌──────────────────────────────────────────────────────────┐
│                  Design Patterns Used                     │
└──────────────────────────────────────────────────────────┘

┌─────────────────────────┐
│ Chain of Responsibility │  ← Primary pattern
├─────────────────────────┤
│ Each formatter tries    │
│ to handle or delegates  │
│ to next in chain        │
└─────────────────────────┘
            │
            │ Implemented via
            ▼
┌─────────────────────────┐
│   Template Method       │  ← Structural support
├─────────────────────────┤
│ AbstractTimeFormatter   │
│ defines algorithm       │
│ structure               │
└─────────────────────────┘
            │
            │ Used by
            ▼
┌─────────────────────────┐
│   Strategy Pattern      │  ← Interface design
├─────────────────────────┤
│ TimeSpokenFormatter     │
│ interface allows        │
│ swapping strategies     │
└─────────────────────────┘
            │
            │ Created via
            ▼
┌─────────────────────────┐
│   Builder Pattern       │  ← Construction
├─────────────────────────┤
│ Fluent API for          │
│ building custom chains  │
└─────────────────────────┘
```

## Summary

This architecture provides:
- ✅ **Clear separation of concerns** - Each formatter has one job
- ✅ **Easy extensibility** - Add new formatters without modifying existing code
- ✅ **Testability** - Each component can be tested independently
- ✅ **Flexibility** - Custom chains can be built for different use cases
- ✅ **Maintainability** - Clear structure makes code easy to understand and modify
