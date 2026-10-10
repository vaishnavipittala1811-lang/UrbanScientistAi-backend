package com.urbanscientist.telangana.repository;

import com.urbanscientist.telangana.model.UrbanLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrbanLocationRepository extends JpaRepository<UrbanLocation, Long> {
    Optional<UrbanLocation> findByLocationId(String locationId);
    List<UrbanLocation> findByDistrictIgnoreCase(String district);
    List<UrbanLocation> findByCityIgnoreCase(String city);

    @Query("SELECT DISTINCT l.district FROM UrbanLocation l ORDER BY l.district ASC")
    List<String> findDistinctDistricts();

    @Query("SELECT DISTINCT l.city FROM UrbanLocation l ORDER BY l.city ASC")
    List<String> findDistinctCities();
}
