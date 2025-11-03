# British Spoken Time Application

This project converts numeric time input into its spoken English (British) equivalent.

## 🧩 Project Overview
This application provides two modes of operation:
1. **Web Wrapper (Spring Boot REST API)** — Expose an endpoint to get spoken time from a web request.
2. **Console Application (Standalone JAR)** — Run directly from the terminal to get spoken time output.

---

## ⚙️ Project Structure

```
com.kamlesh.britishtime
│
├── config
│   └── FormatterConfiguration.java
├── controller
│   └── TimeController.java
├── exception
│   └── InvalidTimeFormatException.java
├── handler
│   └── GlobalExceptionHandler.java
├── model
│   └── SpokenTimeResponse.java
├── service
│   ├── formatter
│   │   ├── BritishTimeFormatter.java
│   │   └── TimeFormatterFactory.java
│   │   └── TimeSpokenFormatter.java   ← Interface implemented by formatters
│   ├── impl
│   │   └── TimeServiceImpl.java
│   └── TimeService.java
├── utility
│   └── TimeParser.java
│
├── BritishSpokenTimeApp.java        ← Console App Main Class
├── BritishSpokenTimeApplication.java← Spring Boot Entry Point

```

---

## 🚀 Run as Spring Boot Web Application

### Prerequisites
- Java 21+
- Maven 3.8+

### Steps
```bash
# 1. Build the application
mvn clean install

# 2. Run the Spring Boot web server
java -jar target/britishtime-1.0.0.jar
```

### API Endpoint
**Request:**
```bash
GET http://localhost:8080/api/time/spoken?time=14:45
```

**Response:**
```json
{
  "input": "14:45",
  "spoken": "quarter to three"
}
```

---

## 💻 Run as Console Application

### Build and Run
```bash
  # Build classes
mvn clean package -Pcli

# Run the console version directly from classes
java -jar target/britishtime-cli-1.0.0.jar 09:45
```

### Example Output
```
quarter to ten
```

---

## 🧪 Run Tests
```bash
  mvn test
```

---

## 🧾 Notes
- Use `TimeSpokenFormatter` for all custom formatter implementations.
- Warnings like `Use --enable-native-access=ALL-UNNAMED` are safe to ignore; they come from Tomcat’s internal native libraries.
- You can rename the final JAR to a friendlier name like `spoken-time.jar` for client distribution.

