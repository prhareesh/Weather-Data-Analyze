package com.timesync.securin.service.ingestion;

import com.timesync.securin.entity.WeatherData;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherCsvParser {

    private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm");
    private static final int EXPECTED_COLUMNS = 20;

    public List<WeatherData> parse(Reader source) throws IOException {
        List<WeatherData> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(source)) {
            String line = reader.readLine(); // header
            if (line == null) {
                return rows;
            }

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                WeatherData record = mapLine(line);
                if (record != null) {
                    rows.add(record);
                }
            }
        }

        return rows;
    }

    private WeatherData mapLine(String line) {
        String[] tokens = line.split(",", -1);
        if (tokens.length < EXPECTED_COLUMNS) {
            return null;
        }

        LocalDateTime dateTime = parseDateTime(tokens[0]);
        if (dateTime == null) {
            return null;
        }

        return WeatherData.builder()
                .dateTime(dateTime)
                .year(dateTime.getYear())
                .month(dateTime.getMonthValue())
                .day(dateTime.getDayOfMonth())
                .weatherCondition(cleanText(tokens[1]))
                .dewPoint(parseNumber(tokens[2]))
                .heatIndex(parseNumber(tokens[5]))
                .humidity(parseNumber(tokens[6]))
                .precipitation(parseNumber(tokens[7]))
                .pressure(parseNumber(tokens[8]))
                .temperature(parseNumber(tokens[11]))
                .windSpeed(parseNumber(tokens[19]))
                .build();
    }

    private LocalDateTime parseDateTime(String raw) {
        String value = cleanText(raw);
        if (value == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, INPUT_DATE_TIME);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Double parseNumber(String raw) {
        String value = cleanText(raw);
        if (value == null || "-9999".equals(value)) {
            return null;
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private String cleanText(String raw) {
        if (raw == null) {
            return null;
        }
        String value = raw.trim();
        return value.isEmpty() ? null : value;
    }
}
