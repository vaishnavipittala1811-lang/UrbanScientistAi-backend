package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.UrbanReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrbanReportRepository extends JpaRepository<UrbanReport, Long> {
    Optional<UrbanReport> findByReportId(String reportId);
    List<UrbanReport> findAllByOrderByGeneratedAtDesc();
}
