package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.WaterRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterRepository extends JpaRepository<WaterRecord, Long> {
    Optional<WaterRecord> findByLocationId(String locationId);
    List<WaterRecord> findByDistrictIgnoreCase(String district);
}
