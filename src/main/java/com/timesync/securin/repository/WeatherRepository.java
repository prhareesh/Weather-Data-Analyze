package com.timesync.securin.repository;


import com.timesync.securin.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, UUID> , JpaSpecificationExecutor<WeatherData> {

}
