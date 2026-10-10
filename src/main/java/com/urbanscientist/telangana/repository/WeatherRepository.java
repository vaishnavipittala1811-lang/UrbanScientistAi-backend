package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherRecord, Long> {
    Optional<WeatherRecord> findByLocationId(String locationId);
    List<WeatherRecord> findByDistrictIgnoreCase(String district);
}
