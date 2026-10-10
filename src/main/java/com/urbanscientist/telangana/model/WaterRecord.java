package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "water_records", indexes = {
    @Index(name = "idx_wtr_loc", columnList = "locationId"),
    @Index(name = "idx_wtr_district", columnList = "district")
})
public class WaterRecord {

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

    private Double groundwaterDepthM;
    private Double groundwaterStressIndex;
    private Double pipedWaterCoveragePct;
    private Double dailyWaterDemandMld;
    private Double dailyWaterSupplyMld;
    private Double supplyDeficitPct;
    private Double waterQualityIndex;
    private Integer tdsPpm;
    private Double waterRiskScore;
    private Double floodVulnerabilityIndex;
    private String timestamp;
    private String dataType = "SYNTHETIC_DEMO";

    public WaterRecord() {}

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

    public Double getGroundwaterDepthM() { return groundwaterDepthM; }
    public void setGroundwaterDepthM(Double groundwaterDepthM) { this.groundwaterDepthM = groundwaterDepthM; }

    public Double getGroundwaterStressIndex() { return groundwaterStressIndex; }
    public void setGroundwaterStressIndex(Double groundwaterStressIndex) { this.groundwaterStressIndex = groundwaterStressIndex; }

    public Double getPipedWaterCoveragePct() { return pipedWaterCoveragePct; }
    public void setPipedWaterCoveragePct(Double pipedWaterCoveragePct) { this.pipedWaterCoveragePct = pipedWaterCoveragePct; }

    public Double getDailyWaterDemandMld() { return dailyWaterDemandMld; }
    public void setDailyWaterDemandMld(Double dailyWaterDemandMld) { this.dailyWaterDemandMld = dailyWaterDemandMld; }

    public Double getDailyWaterSupplyMld() { return dailyWaterSupplyMld; }
    public void setDailyWaterSupplyMld(Double dailyWaterSupplyMld) { this.dailyWaterSupplyMld = dailyWaterSupplyMld; }

    public Double getSupplyDeficitPct() { return supplyDeficitPct; }
    public void setSupplyDeficitPct(Double supplyDeficitPct) { this.supplyDeficitPct = supplyDeficitPct; }

    public Double getWaterQualityIndex() { return waterQualityIndex; }
    public void setWaterQualityIndex(Double waterQualityIndex) { this.waterQualityIndex = waterQualityIndex; }

    public Integer getTdsPpm() { return tdsPpm; }
    public void setTdsPpm(Integer tdsPpm) { this.tdsPpm = tdsPpm; }

    public Double getWaterRiskScore() { return waterRiskScore; }
    public void setWaterRiskScore(Double waterRiskScore) { this.waterRiskScore = waterRiskScore; }

    public Double getFloodVulnerabilityIndex() { return floodVulnerabilityIndex; }
    public void setFloodVulnerabilityIndex(Double floodVulnerabilityIndex) { this.floodVulnerabilityIndex = floodVulnerabilityIndex; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
