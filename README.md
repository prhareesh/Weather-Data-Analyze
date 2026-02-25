A Spring Boot application that processes 20 years of Delhi weather forecast data from CSV files using **Spring Batch**, stores it in a relational database, and exposes REST APIs for filtering and analytics.

---

## 🚀 Features

### ✅ Data Processing

* Upload large CSV weather dataset
* Chunk-based processing using Spring Batch
* Transaction-safe database insertion
* Handles null & malformed values gracefully

### ✅ Weather APIs

* Retrieve weather details by specific date
* Retrieve weather data for a specific month (across all years)
* Extract monthly temperature statistics (min, max, median) for any year

### ✅ Clean Architecture

* Layered modular design
* DTO-based API responses
* Centralized CommonResponse wrapper
* Batch job configuration with JobLauncher

---

# 🏗 Tech Stack

| Technology        | Version |
| ----------------- | ------- |
| Java              | 17      |
| Spring Boot       | 4.0.0   |
| Spring Batch      | 5.x     |
| Spring Data JPA   | ✔       |
| MySQL             | ✔       |
| Lombok            | ✔       |
| Swagger (OpenAPI) | ✔       |

---

# 📂 Project Structure

```
com.timesync.securin
 ├── batch
 │     └── BatchConfig.java
 ├── controller
 │     └── WeatherController.java
 ├── service
 │     ├── WeatherService.java
 │     └── serviceImpl
 │           └── WeatherServiceImpl.java
 ├── repository
 │     └── WeatherRepository.java
 ├── entity
 │     └── WeatherData.java
 ├── dto
 │     ├── WeatherResponse.java
 │     ├── MonthlyStats.java
 │     └── CommonResponse.java
```

---

# ⚙️ Setup Instructions

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/weather-data.git
cd weather-data
```

---

## 2️⃣ Configure Database

Create database:

```sql
CREATE DATABASE weather_db;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/weather_db
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.batch.jdbc.initialize-schema=always
```

---

## 3️⃣ Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

# 📤 CSV Upload API

### Upload Weather Dataset

```
POST /api/weather/upload
```

**Request Type:** `multipart/form-data`

| Key  | Type | Value           |
| ---- | ---- | --------------- |
| file | File | weatherdata.csv |

---

# 🌡 API Endpoints

---

## 🔹 Get Weather by Date

```
GET /api/weather/date?date=2012-05-10
```

Returns:

* Weather condition
* Temperature
* Humidity
* Pressure

---

## 🔹 Get Weather by Month (Across All Years)

```
GET /api/weather/month?month=5
```

Returns all May records across dataset.

---

## 🔹 Get Monthly Temperature Statistics

```
GET /api/weather/stats?year=2015
```

Returns for each month:

* Minimum temperature
* Maximum temperature
* Median temperature

---

# 🧠 How Batch Processing Works

1. CSV uploaded via REST endpoint
2. File saved temporarily
3. JobLauncher triggers Spring Batch Job
4. FlatFileItemReader reads CSV
5. FieldSetMapper converts rows into WeatherData entities
6. JpaItemWriter persists data in chunks
7. Batch metadata stored in BATCH_* tables

---

# 📊 Median Calculation

Median temperature is calculated in the service layer because:

* MySQL does not provide native MEDIAN aggregation
* Ensures database portability
* Avoids vendor-specific SQL

---

# 🔒 Validation & Error Handling

* File type validation (.csv only)
* Null-safe parsing for numeric values
* Structured API responses using CommonResponse
* Prevents duplicate job execution via timestamp parameter

---

# 📖 API Documentation

Swagger UI available at:

```
http://localhost:8080/swagger-ui.html
```

---

# 📈 Future Enhancements

* Pagination & sorting support
* Async batch execution
* Job status monitoring endpoint
* Redis caching
* Docker containerization
* Cloud storage integration

---

# 👨‍💻 Author

Developed as part of a backend technical assessment for weather data processing and analytics.

---

# 🏆 Summary

This project demonstrates:

* Batch processing of large datasets
* REST API development
* Clean architecture principles
* Statistical data computation
* Enterprise-level backend design using Spring Boot

---

If you'd like, I can also generate:

* 🔥 A professional project description for LinkedIn
* 🔥 A GitHub repository description (short version)
* 🔥 A Dockerfile
* 🔥 A Postman collection
* 🔥 A production-level README with architecture diagrams

Just tell me what you need 😎
Perfect 👌
Below is a **clean, professional, GitHub-friendly README.md** — formatted properly for GitHub with badges, sections, and clean Markdown.

You can copy this directly into your project root as:

```
README.md
```

---

# 🌦 Weather Data Processing & Analytics API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.x-brightgreen)
![Spring Batch](https://img.shields.io/badge/Spring%20Batch-5.x-blue)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

A Spring Boot application that processes 20 years of Delhi weather forecast data from CSV files using **Spring Batch**, stores it in a relational database, and exposes REST APIs for filtering and analytics.

---

## 🚀 Features

### ✅ Data Processing

* Upload large CSV weather dataset
* Chunk-based processing using Spring Batch
* Transaction-safe database insertion
* Handles null & malformed values gracefully

### ✅ Weather APIs

* Retrieve weather details by specific date
* Retrieve weather data for a specific month (across all years)
* Extract monthly temperature statistics (min, max, median) for any year

### ✅ Clean Architecture

* Layered modular design
* DTO-based API responses
* Centralized CommonResponse wrapper
* Batch job configuration with JobLauncher

---

# 🏗 Tech Stack

| Technology        | Version |
| ----------------- | ------- |
| Java              | 17      |
| Spring Boot       | 3.3.x   |
| Spring Batch      | 5.x     |
| Spring Data JPA   | ✔       |
| MySQL             | ✔       |
| Lombok            | ✔       |
| Swagger (OpenAPI) | ✔       |

---

# 📂 Project Structure

```
com.timesync.securin
 ├── batch
 │     └── BatchConfig.java
 ├── controller
 │     └── WeatherController.java
 ├── service
 │     ├── WeatherService.java
 │     └── serviceImpl
 │           └── WeatherServiceImpl.java
 ├── repository
 │     └── WeatherRepository.java
 ├── entity
 │     └── WeatherData.java
 ├── dto
 │     ├── WeatherResponse.java
 │     ├── MonthlyStats.java
 │     └── CommonResponse.java
```

---

# ⚙️ Setup Instructions

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/weather-data.git
cd weather-data
```

---

## 2️⃣ Configure Database

Create database:

```sql
CREATE DATABASE weather_db;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/weather_db
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.batch.jdbc.initialize-schema=always
```

---

## 3️⃣ Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

# 📤 CSV Upload API

### Upload Weather Dataset

```
POST /api/weather/upload
```

**Request Type:** `multipart/form-data`

| Key  | Type | Value           |
| ---- | ---- | --------------- |
| file | File | weatherdata.csv |

---

# 🌡 API Endpoints

---

## 🔹 Get Weather by Date

```
GET /api/weather/date?date=2012-05-10
```

Returns:

* Weather condition
* Temperature
* Humidity
* Pressure

---

## 🔹 Get Weather by Month (Across All Years)

```
GET /api/weather/month?month=5
```

Returns all May records across dataset.

---

## 🔹 Get Monthly Temperature Statistics

```
GET /api/weather/stats?year=2015
```

Returns for each month:

* Minimum temperature
* Maximum temperature
* Median temperature

---

# 🧠 How Batch Processing Works

1. CSV uploaded via REST endpoint
2. File saved temporarily
3. JobLauncher triggers Spring Batch Job
4. FlatFileItemReader reads CSV
5. FieldSetMapper converts rows into WeatherData entities
6. JpaItemWriter persists data in chunks
7. Batch metadata stored in BATCH_* tables

---

# 📊 Median Calculation

Median temperature is calculated in the service layer because:

* MySQL does not provide native MEDIAN aggregation
* Ensures database portability
* Avoids vendor-specific SQL

---

# 🔒 Validation & Error Handling

* File type validation (.csv only)
* Null-safe parsing for numeric values
* Structured API responses using CommonResponse
* Prevents duplicate job execution via timestamp parameter

---

# 📖 API Documentation

Swagger UI available at:

```
http://localhost:8080/swagger-ui.html
```

---

# 📈 Future Enhancements

* Pagination & sorting support
* Async batch execution
* Job status monitoring endpoint
* Redis caching
* Docker containerization
* Cloud storage integration



---

# 🏆 Summary

This project demonstrates:

* Batch processing of large datasets
* REST API development
* Clean architecture principles
* Statistical data computation
* Enterprise-level backend design using Spring Boot

