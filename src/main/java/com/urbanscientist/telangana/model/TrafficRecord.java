package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "traffic_records", indexes = {
    @Index(name = "idx_trf_loc", columnList = "locationId"),
    @Index(name = "idx_trf_district", columnList = "district")
})
public class TrafficRecord {

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

    private Double avgSpeedKmh;
    private Double congestionLevelPct;
    private Integer peakHourDelayMin;
    private Integer dailyVehicleCount;
    private Double roadNetworkDensity;
    private Integer incidentFrequencyWeekly;
    private Double publicTransitCoveragePct;
    private Double trafficRiskScore;
    private Integer hotspotFlag;
    private String timestamp;
    private String dataType = "SYNTHETIC_DEMO";

    public TrafficRecord() {}

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

    public Double getAvgSpeedKmh() { return avgSpeedKmh; }
    public void setAvgSpeedKmh(Double avgSpeedKmh) { this.avgSpeedKmh = avgSpeedKmh; }

    public Double getCongestionLevelPct() { return congestionLevelPct; }
    public void setCongestionLevelPct(Double congestionLevelPct) { this.congestionLevelPct = congestionLevelPct; }

    public Integer getPeakHourDelayMin() { return peakHourDelayMin; }
    public void setPeakHourDelayMin(Integer peakHourDelayMin) { this.peakHourDelayMin = peakHourDelayMin; }

    public Integer getDailyVehicleCount() { return dailyVehicleCount; }
    public void setDailyVehicleCount(Integer dailyVehicleCount) { this.dailyVehicleCount = dailyVehicleCount; }

    public Double getRoadNetworkDensity() { return roadNetworkDensity; }
    public void setRoadNetworkDensity(Double roadNetworkDensity) { this.roadNetworkDensity = roadNetworkDensity; }

    public Integer getIncidentFrequencyWeekly() { return incidentFrequencyWeekly; }
    public void setIncidentFrequencyWeekly(Integer incidentFrequencyWeekly) { this.incidentFrequencyWeekly = incidentFrequencyWeekly; }

    public Double getPublicTransitCoveragePct() { return publicTransitCoveragePct; }
    public void setPublicTransitCoveragePct(Double publicTransitCoveragePct) { this.publicTransitCoveragePct = publicTransitCoveragePct; }

    public Double getTrafficRiskScore() { return trafficRiskScore; }
    public void setTrafficRiskScore(Double trafficRiskScore) { this.trafficRiskScore = trafficRiskScore; }

    public Integer getHotspotFlag() { return hotspotFlag; }
    public void setHotspotFlag(Integer hotspotFlag) { this.hotspotFlag = hotspotFlag; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
