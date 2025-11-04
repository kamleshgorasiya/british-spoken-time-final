# British Spoken Time Application

A Spring Boot application that converts numeric time input (HH:mm format) into spoken British English using the **Chain of Responsibility** design pattern.

## 🎯 Features

- ✅ REST API for time conversion
- ✅ Supports multiple time formats (HH:mm, HHmm)
- ✅ Clean architecture with Chain of Responsibility pattern
- ✅ Specialized formatters for different time conditions
- ✅ Comprehensive error handling
- ✅ Full test coverage
- ✅ Easy to extend with custom formatters

---

## 📋 Prerequisites (A-Z Setup)

### A. System Requirements
- **Java**: JDK 21 or higher
- **Maven**: 3.8+ (or use IDE's embedded Maven)
- **IDE** (Optional): IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### B. Verify Java Installation
```bash
# Check Java version
java -version

# Expected output: java version "21.x.x" or higher
```

If Java is not installed:
- **Windows**: Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/)
- **macOS**: `brew install openjdk@21`
- **Linux**: `sudo apt install openjdk-21-jdk` (Ubuntu/Debian)

### C. Verify Maven Installation
```bash
# Check Maven version
mvn -version

# Expected output: Apache Maven 3.8.x or higher
```

If Maven is not installed:
- **Windows**: Download from [Apache Maven](https://maven.apache.org/download.cgi)
- **macOS**: `brew install maven`
- **Linux**: `sudo apt install maven`

### D. Clone/Download the Project
```bash
# If using Git
git clone <repository-url>
cd british-spoken-time-final

# Or download and extract the ZIP file
```

---

## 🏗️ Project Architecture

### Design Pattern: Chain of Responsibility

```
TimeController → TimeServiceImpl → ChainedBritishTimeFormatter
                                            ↓
                    MidnightFormatter → NoonFormatter → OClockFormatter
                    → QuarterPastFormatter → HalfPastFormatter
                    → QuarterToFormatter → MinutesPastFormatter
                    → MinutesToFormatter
```

### Project Structure

```
com.kamlesh.britishtime
│
├── config
│   └── FormatterConfiguration.java          ← Spring Bean Configuration
├── controller
│   └── TimeController.java                  ← REST API Endpoint
├── dtos
│   └── SpokenTimeResponse.java              ← Response DTO
├── exception
│   └── InvalidTimeFormatException.java      ← Custom Exception
├── handler
│   └── GlobalExceptionHandler.java          ← Global Error Handler
├── service
│   ├── formatter
│   │   ├── AbstractTimeFormatter.java       ← Base Formatter (Chain Pattern)
│   │   ├── ChainedBritishTimeFormatter.java ← Composite Formatter
│   │   ├── MidnightFormatter.java           ← Handles 00:00
│   │   ├── NoonFormatter.java               ← Handles 12:00
│   │   ├── OClockFormatter.java             ← Handles XX:00
│   │   ├── QuarterPastFormatter.java        ← Handles XX:15
│   │   ├── HalfPastFormatter.java           ← Handles XX:30
│   │   ├── QuarterToFormatter.java          ← Handles XX:45
│   │   ├── MinutesPastFormatter.java        ← Handles XX:01-30
│   │   ├── MinutesToFormatter.java          ← Handles XX:31-59
│   │   ├── TimeSpokenFormatter.java         ← Strategy Interface
│   │   ├── TimeWords.java                   ← Word Mappings Utility
│   │   └── README.md                        ← Formatter Documentation
│   ├── impl
│   │   └── TimeServiceImpl.java             ← Service Implementation
│   └── TimeService.java                     ← Service Interface
├── utility
│   └── TimeParser.java                      ← Time Parsing Utility
│
└── BritishSpokenTimeApplication.java        ← Spring Boot Entry Point
```

---

## 🚀 How to Build and Run

### Step 1: Build the Application
```bash
# Clean and build the project
mvn clean install

# This will:
# - Compile all Java files
# - Run all tests
# - Create JAR file in target/ directory
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX s
```

### Step 2: Run the Application
```bash
# Run the Spring Boot application
java -jar target/britishtime-1.0.0.jar

# Or using Maven
mvn spring-boot:run
```

**Expected Output:**
```
Started BritishSpokenTimeApplication in X.XXX seconds
Tomcat started on port(s): 8080 (http)
```

### Step 3: Test the API

#### Using cURL
```bash
# Test various times
curl "http://localhost:8080/api/time/spoken?time=00:00"
curl "http://localhost:8080/api/time/spoken?time=12:00"
curl "http://localhost:8080/api/time/spoken?time=03:15"
curl "http://localhost:8080/api/time/spoken?time=14:45"
curl "http://localhost:8080/api/time/spoken?time=09:30"
```

#### Using Browser
Open in your browser:
```
http://localhost:8080/api/time/spoken?time=14:45
```

#### Using Postman
```
GET http://localhost:8080/api/time/spoken?time=14:45
```

---

## 📡 API Documentation

### Endpoint: Get Spoken Time

**URL:** `/api/time/spoken`  
**Method:** `GET`  
**Query Parameter:** `time` (required)

#### Supported Time Formats
- `HH:mm` - e.g., `03:15`, `14:45`
- `HHmm` - e.g., `0315`, `1445`

#### Example Requests & Responses

| Input | Output |
|-------|--------|
| `00:00` | `{"input":"00:00","spoken":"midnight"}` |
| `12:00` | `{"input":"12:00","spoken":"noon"}` |
| `03:00` | `{"input":"03:00","spoken":"three o'clock"}` |
| `03:15` | `{"input":"03:15","spoken":"quarter past three"}` |
| `03:30` | `{"input":"03:30","spoken":"half past three"}` |
| `03:45` | `{"input":"03:45","spoken":"quarter to four"}` |
| `03:05` | `{"input":"03:05","spoken":"five past three"}` |
| `03:55` | `{"input":"03:55","spoken":"five to four"}` |
| `14:45` | `{"input":"14:45","spoken":"quarter to three"}` |

#### Error Responses

**Invalid Time Format:**
```json
{
  "error": "Invalid time format",
  "message": "Time must be in HH:mm or HHmm format"
}
```

**Missing Parameter:**
```json
{
  "error": "Bad Request",
  "message": "Required parameter 'time' is missing"
}
```

---

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=ChainedBritishTimeFormatterTest
mvn test -Dtest=IndividualFormatterTest
```

### Run Tests with Coverage
```bash
mvn clean test jacoco:report
```

### Test Structure
```
src/test/java/com/kamlesh/britishtime/
├── formatter/
│   ├── ChainedBritishTimeFormatterTest.java  ← Integration tests
│   └── IndividualFormatterTest.java          ← Unit tests
├── BritishTimeFormatterTest.java             ← Legacy formatter tests
├── TimeControllerIntegrationTest.java        ← API tests
└── TimeParserTest.java                       ← Parser tests
```

---

## 🔧 Configuration

### Application Properties
Location: `src/main/resources/application.properties`

```properties
# Server Configuration
server.port=8080

# Application Configuration
app.locale=en-GB

# Logging
logging.level.com.kamlesh.britishtime=INFO
```

### Change Server Port
```properties
server.port=9090
```

Then access API at: `http://localhost:9090/api/time/spoken?time=03:15`

---

## 🎨 Extending the Application

### Adding a Custom Formatter

#### Step 1: Create Your Formatter
```java
package com.kamlesh.britishtime.service.formatter;

import java.time.LocalTime;
import java.util.Optional;

public class DawnFormatter extends AbstractTimeFormatter {
    @Override
    public Optional<String> tryFormat(LocalTime time) {
        if (time.getHour() == 5 && time.getMinute() == 0) {
            return Optional.of("dawn");
        }
        return Optional.empty();
    }
}
```

#### Step 2: Update Configuration
Edit `FormatterConfiguration.java`:
```java
@Bean
public TimeSpokenFormatter timeSpokenFormatter() {
    return new ChainedBritishTimeFormatter.Builder()
        .addFormatter(new MidnightFormatter())
        .addFormatter(new NoonFormatter())
        .addFormatter(new DawnFormatter())  // Add your formatter
        .addFormatter(new OClockFormatter())
        // ... rest of chain
        .build();
}
```

#### Step 3: Write Tests
```java
@Test
void testDawnFormatter() {
    DawnFormatter formatter = new DawnFormatter();
    assertEquals(Optional.of("dawn"), 
                 formatter.tryFormat(LocalTime.of(5, 0)));
}
```

See `QUICK_START_GUIDE.md` for more examples.

---

## 📚 Additional Documentation

- **FORMATTER_ARCHITECTURE.md** - Complete architecture documentation
- **ARCHITECTURE_DIAGRAM.md** - Visual diagrams and flows
- **QUICK_START_GUIDE.md** - 5-minute tutorial for adding formatters
- **src/main/java/.../formatter/README.md** - Formatter package documentation

---

## 🐛 Troubleshooting

### Issue: Port 8080 already in use
**Solution:** Change port in `application.properties` or kill the process using port 8080
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Issue: Java version mismatch
**Solution:** Ensure Java 21+ is installed and set as JAVA_HOME
```bash
# Check Java version
java -version

# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\Program Files\Java\jdk-21

# Set JAVA_HOME (Linux/Mac)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
```

### Issue: Tests failing
**Solution:** Run with clean build
```bash
mvn clean test
```

### Issue: Build fails with compilation errors
**Solution:** Ensure Maven is using Java 21
```bash
mvn -version  # Check Maven's Java version
```

---

## 📝 Notes

- **Design Pattern**: Uses Chain of Responsibility for clean separation of concerns
- **Extensibility**: Easy to add new time formatters without modifying existing code
- **Testing**: Comprehensive unit and integration tests included
- **Spring Boot**: Version 3.2.6 with Java 21
- **API**: RESTful design with proper error handling

---

## 🤝 Contributing

To add new features:
1. Create a new formatter extending `AbstractTimeFormatter`
2. Add it to the chain in `FormatterConfiguration`
3. Write tests for your formatter
4. Update documentation

---

## 📄 License

This project is for educational and demonstration purposes.

---

## 👨‍💻 Author

**Kamlesh Gorasiya**  
Demonstrating clean architecture with Chain of Responsibility pattern

