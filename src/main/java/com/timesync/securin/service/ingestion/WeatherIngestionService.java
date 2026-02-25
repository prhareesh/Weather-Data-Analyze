package com.timesync.securin.service.ingestion;

import com.timesync.securin.entity.WeatherData;
import com.timesync.securin.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherIngestionService {

    private static final int BATCH_SIZE = 2000;

    private final WeatherRepository weatherRepository;
    private final WeatherCsvParser weatherCsvParser;

    public int ingest(MultipartFile file) throws IOException {
        List<WeatherData> rows = weatherCsvParser.parse(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        );

        weatherRepository.deleteAllInBatch();
        for (int start = 0; start < rows.size(); start += BATCH_SIZE) {
            int end = Math.min(start + BATCH_SIZE, rows.size());
            weatherRepository.saveAll(rows.subList(start, end));
        }

        return rows.size();
    }
}
