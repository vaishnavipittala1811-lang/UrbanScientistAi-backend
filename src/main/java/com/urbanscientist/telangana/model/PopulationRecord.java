package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "population_records", indexes = {
    @Index(name = "idx_pop_loc", columnList = "locationId"),
    @Index(name = "idx_pop_district", columnList = "district")
})
public class PopulationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String locationId;

    @Column(nullable = false)
    private String state = "Telangana";

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String zone;

    private Long currentPopulation;
    private Integer populationDensityPerSqkm;
    private Double annualGrowthRatePct;
    private Double urbanizationRatePct;
    private Long forecast5yr;
    private Long forecast10yr;
    private Long forecast15yr;
    private Double migrantInfluxRate;
    private Double dependencyRatio;
    private Double populationPressureScore;
    private String dataType = "SYNTHETIC_DEMO";

    public PopulationRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public Long getCurrentPopulation() { return currentPopulation; }
    public void setCurrentPopulation(Long currentPopulation) { this.currentPopulation = currentPopulation; }

    public Integer getPopulationDensityPerSqkm() { return populationDensityPerSqkm; }
    public void setPopulationDensityPerSqkm(Integer populationDensityPerSqkm) { this.populationDensityPerSqkm = populationDensityPerSqkm; }

    public Double getAnnualGrowthRatePct() { return annualGrowthRatePct; }
    public void setAnnualGrowthRatePct(Double annualGrowthRatePct) { this.annualGrowthRatePct = annualGrowthRatePct; }

    public Double getUrbanizationRatePct() { return urbanizationRatePct; }
    public void setUrbanizationRatePct(Double urbanizationRatePct) { this.urbanizationRatePct = urbanizationRatePct; }

    public Long getForecast5yr() { return forecast5yr; }
    public void setForecast5yr(Long forecast5yr) { this.forecast5yr = forecast5yr; }

    public Long getForecast10yr() { return forecast10yr; }
    public void setForecast10yr(Long forecast10yr) { this.forecast10yr = forecast10yr; }

    public Long getForecast15yr() { return forecast15yr; }
    public void setForecast15yr(Long forecast15yr) { this.forecast15yr = forecast15yr; }

    public Double getMigrantInfluxRate() { return migrantInfluxRate; }
    public void setMigrantInfluxRate(Double migrantInfluxRate) { this.migrantInfluxRate = migrantInfluxRate; }

    public Double getDependencyRatio() { return dependencyRatio; }
    public void setDependencyRatio(Double dependencyRatio) { this.dependencyRatio = dependencyRatio; }

    public Double getPopulationPressureScore() { return populationPressureScore; }
    public void setPopulationPressureScore(Double populationPressureScore) { this.populationPressureScore = populationPressureScore; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
