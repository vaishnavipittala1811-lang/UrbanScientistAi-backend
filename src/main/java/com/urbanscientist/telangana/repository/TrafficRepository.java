package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.TrafficRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrafficRepository extends JpaRepository<TrafficRecord, Long> {
    Optional<TrafficRecord> findByLocationId(String locationId);
    List<TrafficRecord> findByDistrictIgnoreCase(String district);
    List<TrafficRecord> findByHotspotFlag(Integer hotspotFlag);
}
