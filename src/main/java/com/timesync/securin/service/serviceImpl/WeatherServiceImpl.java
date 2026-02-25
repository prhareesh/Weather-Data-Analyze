package com.timesync.securin.service.serviceImpl;

import com.timesync.securin.dto.CommonResponse;
import com.timesync.securin.dto.MonthlyStats;
import com.timesync.securin.dto.WeatherResponse;
import com.timesync.securin.entity.ResponseStatus;
import com.timesync.securin.entity.WeatherData;
import com.timesync.securin.repository.WeatherRepository;
import com.timesync.securin.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final JobLauncher jobLauncher;
    private final Job weatherJob;
    private final WeatherRepository repository;

    @Override
    public CommonResponse upload(MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                return CommonResponse.builder()
                        .errorMessage("Please Insert a file")
                        .code(400)
                        .status(ResponseStatus.FAILURE)
                        .build();
            }

            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + File.separator + fileName;

            File dest = new File(filePath);
            file.transferTo(dest);

            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(weatherJob, params);

            return CommonResponse.builder()
                    .successMessage("Upload Success")
                    .code(200)
                    .status(ResponseStatus.SUCCESS)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.builder()
                    .errorMessage("Upload failed: " + e.getMessage())
                    .code(500)
                    .status(ResponseStatus.FAILURE)
                    .build();
        }
    }

    @Override
    public CommonResponse getByDate(LocalDate date) {

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

        if (month < 1 || month > 12) {
            return CommonResponse.builder()
                    .status(ResponseStatus.FAILURE)
                    .errorMessage("Invalid month. Must be between 1 and 12")
                    .code(400)
                    .build();
        }

        List<WeatherResponse> result =
                repository.findByMonth(month)
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

        List<MonthlyStats> statsList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            List<Double> temps = repository.findByYearAndMonth(year, month)
                    .stream()
                    .map(WeatherData::getTemperature)
                    .filter(Objects::nonNull)  // IMPORTANT
                    .sorted()
                    .toList();

            if (temps.isEmpty()) continue;

            double min = temps.get(0);
            double max = temps.get(temps.size() - 1);

            double median;
            int size = temps.size();
            if (size % 2 == 0) {
                median = (temps.get(size/2 - 1) + temps.get(size/2)) / 2.0;
            } else {
                median = temps.get(size/2);
            }

            statsList.add(
                    new MonthlyStats(month, min, max, median)
            );
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
