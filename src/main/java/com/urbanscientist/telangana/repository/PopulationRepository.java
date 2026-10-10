package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.PopulationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PopulationRepository extends JpaRepository<PopulationRecord, Long> {
    Optional<PopulationRecord> findByLocationId(String locationId);
    List<PopulationRecord> findByDistrictIgnoreCase(String district);
}
