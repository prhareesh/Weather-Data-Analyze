package com.timesync.securin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class MonthlyStats {

    private Integer month;
    private Double minTemperature;
    private Double maxTemperature;
    private Double medianTemperature;
}