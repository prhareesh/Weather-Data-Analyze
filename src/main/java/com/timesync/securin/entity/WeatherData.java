package com.timesync.securin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name="Weather_Data_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherData {

    @Id
    @GeneratedValue( strategy =  GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    private Integer year;
    private Integer month;
    private Integer day;

    private Double temperature;   // _tempm
    private Double humidity;      // _hum
    private Double pressure;      // _pressurem
    private Double heatIndex;     // _heatindexm
    private Double dewPoint;      // _dewptm
    private Double precipitation; // _precipm
    private Double windSpeed;     // _wspdm

    private String weatherCondition; // _conds
}
