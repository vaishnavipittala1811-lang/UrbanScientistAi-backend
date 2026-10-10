package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "weather_records", indexes = {
    @Index(name = "idx_wth_loc", columnList = "locationId"),
    @Index(name = "idx_wth_district", columnList = "district")
})
public class WeatherRecord {

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

    private Double temperatureC;
    private Double heatIndexC;
    private Double humidityPct;
    private Double rainfallMmMonthly;
    private Double windSpeedKmh;
    private Double surfaceHeatIslandIntensityC;
    private Double monsoonFloodRiskPct;
    private Double droughtRiskPct;
    private String timestamp;
    private String dataType = "SYNTHETIC_DEMO";

    public WeatherRecord() {}

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

    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }

    public Double getHeatIndexC() { return heatIndexC; }
    public void setHeatIndexC(Double heatIndexC) { this.heatIndexC = heatIndexC; }

    public Double getHumidityPct() { return humidityPct; }
    public void setHumidityPct(Double humidityPct) { this.humidityPct = humidityPct; }

    public Double getRainfallMmMonthly() { return rainfallMmMonthly; }
    public void setRainfallMmMonthly(Double rainfallMmMonthly) { this.rainfallMmMonthly = rainfallMmMonthly; }

    public Double getWindSpeedKmh() { return windSpeedKmh; }
    public void setWindSpeedKmh(Double windSpeedKmh) { this.windSpeedKmh = windSpeedKmh; }

    public Double getSurfaceHeatIslandIntensityC() { return surfaceHeatIslandIntensityC; }
    public void setSurfaceHeatIslandIntensityC(Double surfaceHeatIslandIntensityC) { this.surfaceHeatIslandIntensityC = surfaceHeatIslandIntensityC; }

    public Double getMonsoonFloodRiskPct() { return monsoonFloodRiskPct; }
    public void setMonsoonFloodRiskPct(Double monsoonFloodRiskPct) { this.monsoonFloodRiskPct = monsoonFloodRiskPct; }

    public Double getDroughtRiskPct() { return droughtRiskPct; }
    public void setDroughtRiskPct(Double droughtRiskPct) { this.droughtRiskPct = droughtRiskPct; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
