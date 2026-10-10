package com.urbanscientist.telangana.dto;

import java.util.Map;

public class UrbanHealthScoreDto {

    private String geographicScope = "Telangana, India";
    private Double overallScore;
    private String category; // Excellent, Good, Moderate, High Risk, Critical
    private Double trafficHealth;
    private Double pollutionHealth;
    private Double waterHealth;
    private Double healthcareHealth;
    private Double infrastructureHealth;
    private Double populationHealth;
    private Integer totalLocationsMonitored;
    private Integer activeHotspotsCount;
    private Map<String, Double> districtBreakdown;

    public UrbanHealthScoreDto() {}

    public String getGeographicScope() { return geographicScope; }
    public void setGeographicScope(String geographicScope) { this.geographicScope = geographicScope; }

    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getTrafficHealth() { return trafficHealth; }
    public void setTrafficHealth(Double trafficHealth) { this.trafficHealth = trafficHealth; }

    public Double getPollutionHealth() { return pollutionHealth; }
    public void setPollutionHealth(Double pollutionHealth) { this.pollutionHealth = pollutionHealth; }

    public Double getWaterHealth() { return waterHealth; }
    public void setWaterHealth(Double waterHealth) { this.waterHealth = waterHealth; }

    public Double getHealthcareHealth() { return healthcareHealth; }
    public void setHealthcareHealth(Double healthcareHealth) { this.healthcareHealth = healthcareHealth; }

    public Double getInfrastructureHealth() { return infrastructureHealth; }
    public void setInfrastructureHealth(Double infrastructureHealth) { this.infrastructureHealth = infrastructureHealth; }

    public Double getPopulationHealth() { return populationHealth; }
    public void setPopulationHealth(Double populationHealth) { this.populationHealth = populationHealth; }

    public Integer getTotalLocationsMonitored() { return totalLocationsMonitored; }
    public void setTotalLocationsMonitored(Integer totalLocationsMonitored) { this.totalLocationsMonitored = totalLocationsMonitored; }

    public Integer getActiveHotspotsCount() { return activeHotspotsCount; }
    public void setActiveHotspotsCount(Integer activeHotspotsCount) { this.activeHotspotsCount = activeHotspotsCount; }

    public Map<String, Double> getDistrictBreakdown() { return districtBreakdown; }
    public void setDistrictBreakdown(Map<String, Double> districtBreakdown) { this.districtBreakdown = districtBreakdown; }
}
