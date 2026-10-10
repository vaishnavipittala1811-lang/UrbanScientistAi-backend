package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "healthcare_records", indexes = {
    @Index(name = "idx_hc_loc", columnList = "locationId"),
    @Index(name = "idx_hc_district", columnList = "district")
})
public class HealthcareRecord {

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

    private Double hospitalBedsPer1000;
    private Integer primaryHealthCenters;
    private Double icuOccupancyRatePct;
    private Double doctorToPopRatio;
    private Double ambulanceAvgResponseMin;
    private Double maternalHealthCoveragePct;
    private Double epidemicVulnerabilityIndex;
    private Double healthcarePressureScore;
    private Integer criticalShortageFlag;
    private String dataType = "SYNTHETIC_DEMO";

    public HealthcareRecord() {}

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

    public Double getHospitalBedsPer1000() { return hospitalBedsPer1000; }
    public void setHospitalBedsPer1000(Double hospitalBedsPer1000) { this.hospitalBedsPer1000 = hospitalBedsPer1000; }

    public Integer getPrimaryHealthCenters() { return primaryHealthCenters; }
    public void setPrimaryHealthCenters(Integer primaryHealthCenters) { this.primaryHealthCenters = primaryHealthCenters; }

    public Double getIcuOccupancyRatePct() { return icuOccupancyRatePct; }
    public void setIcuOccupancyRatePct(Double icuOccupancyRatePct) { this.icuOccupancyRatePct = icuOccupancyRatePct; }

    public Double getDoctorToPopRatio() { return doctorToPopRatio; }
    public void setDoctorToPopRatio(Double doctorToPopRatio) { this.doctorToPopRatio = doctorToPopRatio; }

    public Double getAmbulanceAvgResponseMin() { return ambulanceAvgResponseMin; }
    public void setAmbulanceAvgResponseMin(Double ambulanceAvgResponseMin) { this.ambulanceAvgResponseMin = ambulanceAvgResponseMin; }

    public Double getMaternalHealthCoveragePct() { return maternalHealthCoveragePct; }
    public void setMaternalHealthCoveragePct(Double maternalHealthCoveragePct) { this.maternalHealthCoveragePct = maternalHealthCoveragePct; }

    public Double getEpidemicVulnerabilityIndex() { return epidemicVulnerabilityIndex; }
    public void setEpidemicVulnerabilityIndex(Double epidemicVulnerabilityIndex) { this.epidemicVulnerabilityIndex = epidemicVulnerabilityIndex; }

    public Double getHealthcarePressureScore() { return healthcarePressureScore; }
    public void setHealthcarePressureScore(Double healthcarePressureScore) { this.healthcarePressureScore = healthcarePressureScore; }

    public Integer getCriticalShortageFlag() { return criticalShortageFlag; }
    public void setCriticalShortageFlag(Integer criticalShortageFlag) { this.criticalShortageFlag = criticalShortageFlag; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
