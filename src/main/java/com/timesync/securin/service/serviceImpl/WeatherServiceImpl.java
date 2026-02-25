package com.timesync.securin.service.serviceImpl;

import com.timesync.securin.dto.CommonResponse;
import com.timesync.securin.dto.MonthlyStats;
import com.timesync.securin.dto.WeatherResponse;
import com.timesync.securin.entity.ResponseStatus;
import com.timesync.securin.entity.WeatherData;
import com.timesync.securin.repository.WeatherRepository;
import com.timesync.securin.service.WeatherService;
import com.timesync.securin.service.ingestion.WeatherIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherIngestionService ingestionService;
    private final WeatherRepository repository;

    @Override
    public CommonResponse upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return CommonResponse.builder()
                    .errorMessage("Please upload a CSV file")
                    .code(400)
                    .status(ResponseStatus.FAILURE)
                    .build();
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".csv")) {
            return CommonResponse.builder()
                    .errorMessage("Only .csv files are supported")
                    .code(400)
                    .status(ResponseStatus.FAILURE)
                    .build();
        }

        try {
            int recordsSaved = ingestionService.ingest(file);
            if (recordsSaved == 0) {
                return CommonResponse.builder()
                        .errorMessage("Upload parsed 0 rows. Check CSV format/header.")
                        .code(400)
                        .status(ResponseStatus.FAILURE)
                        .build();
            }
            return CommonResponse.builder()
                    .successMessage("Upload successful. Records stored: " + recordsSaved)
                    .code(200)
                    .status(ResponseStatus.SUCCESS)
                    .build();
        } catch (Exception e) {
            return CommonResponse.builder()
                    .errorMessage("Upload failed: " + e.getMessage())
                    .code(500)
                    .status(ResponseStatus.FAILURE)
                    .build();
        }
    }

    @Override
    public CommonResponse getByDate(LocalDate date) {
        if (date == null) {
            return CommonResponse.builder()
                    .status(ResponseStatus.FAILURE)
                    .errorMessage("Date is required. Format: yyyy-MM-dd")
                    .code(400)
                    .build();
        }

        List<WeatherResponse> result =
                repository.findByDateTimeBetween(
                                date.atStartOfDay(),
                                date.atTime(23, 59, 59)
                        )
                        .stream()
                        .map(this::mapToDTO)
                        .toList();

        return CommonResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .successMessage("Weather data fetched successfully")
                .data(result)
                .code(200)
                .build();
    }

    @Override
    public CommonResponse getByMonth(Integer month) {
        if (month == null || month < 1 || month > 12) {
            return CommonResponse.builder()
                    .status(ResponseStatus.FAILURE)
                    .errorMessage("Invalid month. Must be between 1 and 12")
                    .code(400)
                    .build();
        }

        List<WeatherResponse> result =
                repository.findAllByMonth(month)
                        .stream()
                        .map(this::mapToDTO)
                        .toList();

        return CommonResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .successMessage("Weather data fetched successfully")
                .data(result)
                .code(200)
                .build();
    }

    @Override
    public CommonResponse getYearlyStats(Integer year) {
        if (year == null) {
            return CommonResponse.builder()
                    .status(ResponseStatus.FAILURE)
                    .errorMessage("Year is required")
                    .code(400)
                    .build();
        }

        List<MonthlyStats> statsList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            List<Double> temps = repository.findSortedTemperaturesByYearAndMonth(year, month);
            if (temps.isEmpty()) {
                continue;
            }

            double min = temps.get(0);
            double max = temps.get(temps.size() - 1);

            double median;
            int size = temps.size();
            if (size % 2 == 0) {
                median = (temps.get(size / 2 - 1) + temps.get(size / 2)) / 2.0;
            } else {
                median = temps.get(size / 2);
            }

            statsList.add(new MonthlyStats(month, min, max, median));
        }

        return CommonResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .successMessage("Yearly temperature stats calculated")
                .data(statsList)
                .code(200)
                .build();
    }

    private WeatherResponse mapToDTO(WeatherData data) {
        return WeatherResponse.builder()
                .date(data.getDateTime().toLocalDate())
                .temperature(data.getTemperature())
                .humidity(data.getHumidity())
                .pressure(data.getPressure())
                .weatherCondition(data.getWeatherCondition())
                .build();
    }
}
