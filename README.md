# Securin Weather Data API

Spring Boot backend for uploading and querying Delhi weather data from CSV files.

## What This Project Does

- Uploads weather CSV files and stores parsed rows in a relational database.
- Exposes APIs to:
  - fetch weather details by exact date,
  - fetch weather details by month across all years,
  - fetch monthly temperature stats (min, max, median) for a given year.

## Tech Stack

- Java 17
- Spring Boot 4.0.3
- Spring Web
- Spring Data JPA
- MySQL Connector/J
- Springdoc OpenAPI (Swagger UI)
- Lombok
- H2 (runtime/testing utility)

## Project Structure

- `src/main/java/com/timesync/securin/controller` : REST endpoints
- `src/main/java/com/timesync/securin/service` : service contracts and implementation
- `src/main/java/com/timesync/securin/service/ingestion` : CSV parsing and ingestion logic
- `src/main/java/com/timesync/securin/repository` : JPA repository layer
- `src/main/java/com/timesync/securin/entity` : JPA entities
- `src/main/java/com/timesync/securin/dto` : API response DTOs

## Configuration

Default app settings are in `src/main/resources/application.properties`.

By default, it uses MySQL:

- `DB_URL` (default: `jdbc:mysql://localhost:3306/securin`)
- `DB_USERNAME` (default: `root`)
- `DB_PASSWORD` (default: `1234`)
- `DB_DRIVER` (default: `com.mysql.cj.jdbc.Driver`)

Server runs on port `8081`.

## Run Locally

1. Create database:

```sql
CREATE DATABASE securin;
```

2. Start application:

```bash
./mvnw spring-boot:run
```

(Windows PowerShell: `mvnw spring-boot:run`)

3. Swagger UI:

- `http://localhost:8081/swagger-ui/index.html`

## CSV Format Expected

The upload parser expects a 20-column CSV with a header similar to:

```csv
datetime_utc,_conds,_dewptm,_fog,_hail,_heatindexm,_hum,_precipm,_pressurem,_rain,_snow,_tempm,_thunder,_tornado,_vism,_wdird,_wdire,_wgustm,_windchillm,_wspdm
```

Important parser rules:

- Date-time format: `yyyyMMdd-HH:mm` (example: `19961101-11:00`)
- `-9999` and empty numeric values are treated as `null`
- Non-CSV files are rejected

## API Endpoints

Base path: `/api/weather`

1. Upload CSV

- `POST /api/weather/upload`
- Content type: `multipart/form-data`
- Form field: `file`

Example cURL:

```bash
curl -X POST "http://localhost:8081/api/weather/upload" \
  -F "file=@testset.csv"
```

2. Get weather by date

- `GET /api/weather/date?date=1996-11-01`
- Returns weather condition, temperature, humidity, pressure for records on that date.

3. Get weather by month (all years)

- `GET /api/weather/month?month=11`

4. Get yearly monthly temperature stats

- `GET /api/weather/stats?year=1996`
- For each available month in that year, returns:
  - `minTemperature`
  - `maxTemperature`
  - `medianTemperature`

## Response Contract

All endpoints return a common wrapper:

- `status` (`SUCCESS` / `FAILURE`)
- `errorMessage`
- `successMessage`
- `data`
- `code`

## Notes

- Upload currently replaces existing data (`deleteAllInBatch`) before inserting new rows.
- If upload parses zero rows, API returns failure with a format hint.

## Build and Test

```bash
./mvnw test
./mvnw clean package
```

## Future Improvements

- Add pagination for `/month` and `/date`
- Add row-level ingest error report (skipped row count + reasons)
- Add integration tests for upload and query endpoints
- Optional: switch to a dedicated CSV library if input format becomes complex (quoted commas, multiline fields)
