package com.timesync.securin.repository;


import com.timesync.securin.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, UUID> , JpaSpecificationExecutor<WeatherData> {
        List<WeatherData> findByMonth(Integer month);

        List<WeatherData> findByYearAndMonth(Integer year, Integer month);

        List<WeatherData> findByDateTimeBetween(
                LocalDateTime start,
                LocalDateTime end
        );
    }

