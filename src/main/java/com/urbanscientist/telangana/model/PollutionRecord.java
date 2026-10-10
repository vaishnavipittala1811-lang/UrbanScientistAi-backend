package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pollution_records", indexes = {
    @Index(name = "idx_pol_loc", columnList = "locationId"),
    @Index(name = "idx_pol_district", columnList = "district")
})
public class PollutionRecord {

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

    private Double pm25;
    private Double pm10;
    private Double no2;
    private Double so2;
    private Double co;
    private Integer aqi;
    private String aqiCategory;
    private Double industrialEmissionIndex;
    private Double pollutionRiskScore;
    private String dominantPollutant;
    private String timestamp;
    private String dataType = "SYNTHETIC_DEMO";

    public PollutionRecord() {}

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

    public Double getPm25() { return pm25; }
    public void setPm25(Double pm25) { this.pm25 = pm25; }

    public Double getPm10() { return pm10; }
    public void setPm10(Double pm10) { this.pm10 = pm10; }

    public Double getNo2() { return no2; }
    public void setNo2(Double no2) { this.no2 = no2; }

    public Double getSo2() { return so2; }
    public void setSo2(Double so2) { this.so2 = so2; }

    public Double getCo() { return co; }
    public void setCo(Double co) { this.co = co; }

    public Integer getAqi() { return aqi; }
    public void setAqi(Integer aqi) { this.aqi = aqi; }

    public String getAqiCategory() { return aqiCategory; }
    public void setAqiCategory(String aqiCategory) { this.aqiCategory = aqiCategory; }

    public Double getIndustrialEmissionIndex() { return industrialEmissionIndex; }
    public void setIndustrialEmissionIndex(Double industrialEmissionIndex) { this.industrialEmissionIndex = industrialEmissionIndex; }

    public Double getPollutionRiskScore() { return pollutionRiskScore; }
    public void setPollutionRiskScore(Double pollutionRiskScore) { this.pollutionRiskScore = pollutionRiskScore; }

    public String getDominantPollutant() { return dominantPollutant; }
    public void setDominantPollutant(String dominantPollutant) { this.dominantPollutant = dominantPollutant; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
