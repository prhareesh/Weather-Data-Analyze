package com.timesync.securin.repository;


import com.timesync.securin.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, Long> {

    @Query("SELECT w FROM WeatherData w WHERE w.month = :month ORDER BY w.dateTime ASC")
    List<WeatherData> findAllByMonth(@Param("month") Integer month);

    List<WeatherData> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT w.temperature
            FROM WeatherData w
            WHERE w.year = :year
              AND w.month = :month
              AND w.temperature IS NOT NULL
            ORDER BY w.temperature ASC
            """)
    List<Double> findSortedTemperaturesByYearAndMonth(
            @Param("year") Integer year,
            @Param("month") Integer month
    );
}

