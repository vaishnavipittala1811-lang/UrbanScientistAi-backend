package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.InfrastructureRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InfrastructureRepository extends JpaRepository<InfrastructureRecord, Long> {
    Optional<InfrastructureRecord> findByLocationId(String locationId);
    List<InfrastructureRecord> findByDistrictIgnoreCase(String district);
}
