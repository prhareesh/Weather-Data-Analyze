package com.timesync.securin.service;

import com.timesync.securin.dto.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


public interface WeatherService {

    CommonResponse upload(MultipartFile file);
    CommonResponse getByDate(LocalDate date);

    CommonResponse getByMonth(Integer month);

    CommonResponse getYearlyStats(Integer year);

}
