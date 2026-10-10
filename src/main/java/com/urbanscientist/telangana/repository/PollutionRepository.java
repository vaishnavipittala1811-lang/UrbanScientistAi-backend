package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.PollutionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PollutionRepository extends JpaRepository<PollutionRecord, Long> {
    Optional<PollutionRecord> findByLocationId(String locationId);
    List<PollutionRecord> findByDistrictIgnoreCase(String district);
    List<PollutionRecord> findByAqiGreaterThanEqual(Integer aqiThreshold);
}
