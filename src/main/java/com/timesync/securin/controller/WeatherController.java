package com.timesync.securin.controller;


import com.timesync.securin.dto.CommonResponse;
import com.timesync.securin.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Reader;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {


        private final WeatherService service;

        @PostMapping("/upload")
        public ResponseEntity<CommonResponse> upload(@RequestParam("file") MultipartFile file) {
            CommonResponse response = service.upload(file);
            return ResponseEntity.status(response.getCode()).body(response);
        }

        @GetMapping("/date")
        public ResponseEntity<CommonResponse> getByDate(
                @RequestParam LocalDate date) {
            CommonResponse response = service.getByDate(date);

            return ResponseEntity.status(response.getCode()).body(response);
        }

        @GetMapping("/month")
        public ResponseEntity<CommonResponse> getByMonth(
                @RequestParam Integer month) {
            CommonResponse response = service.getByMonth(month);
            return ResponseEntity.status(response.getCode()).body(response);
        }

        @GetMapping("/stats")
        public ResponseEntity<CommonResponse> getYearlyStats(
                @RequestParam Integer year) {
            CommonResponse response = service.getYearlyStats(year);

            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

