package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.HealthcareRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthcareRepository extends JpaRepository<HealthcareRecord, Long> {
    Optional<HealthcareRecord> findByLocationId(String locationId);
    List<HealthcareRecord> findByDistrictIgnoreCase(String district);
    List<HealthcareRecord> findByCriticalShortageFlag(Integer flag);
}
