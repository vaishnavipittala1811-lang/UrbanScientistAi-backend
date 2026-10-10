package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    Optional<Intervention> findByInterventionId(String interventionId);
    List<Intervention> findByCategoryIgnoreCase(String category);
}
