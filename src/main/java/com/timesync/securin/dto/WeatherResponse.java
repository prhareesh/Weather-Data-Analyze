package com.timesync.securin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class WeatherResponse {

    private LocalDate date;
    private Double temperature;
    private Double humidity;
    private Double pressure;
    private String weatherCondition;
}