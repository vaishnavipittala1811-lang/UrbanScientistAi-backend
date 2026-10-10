package com.urbanscientist.telangana.dto;

public class SimulationResult {

    private String geographicScope = "Telangana, India";
    private String location;
    private String district;
    private String interventionName;
    private String category;
    private Double currentTrafficRisk;
    private Double projectedTrafficRisk;
    private Double currentPollutionRisk;
    private Double projectedPollutionRisk;
    private Double currentWaterRisk;
    private Double projectedWaterRisk;
    private Double currentOverallRisk;
    private Double projectedOverallRisk;
    private Double riskReductionPct;
    private Long populationBenefited;
    private Double estimatedCostInrCrores;
    private Long co2ReductionTonsYr;
    private Double estimatedRoi;
    private String scientificRationale;

    public SimulationResult() {}

    public String getGeographicScope() { return geographicScope; }
    public void setGeographicScope(String geographicScope) { this.geographicScope = geographicScope; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getInterventionName() { return interventionName; }
    public void setInterventionName(String interventionName) { this.interventionName = interventionName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getCurrentTrafficRisk() { return currentTrafficRisk; }
    public void setCurrentTrafficRisk(Double currentTrafficRisk) { this.currentTrafficRisk = currentTrafficRisk; }

    public Double getProjectedTrafficRisk() { return projectedTrafficRisk; }
    public void setProjectedTrafficRisk(Double projectedTrafficRisk) { this.projectedTrafficRisk = projectedTrafficRisk; }

    public Double getCurrentPollutionRisk() { return currentPollutionRisk; }
    public void setCurrentPollutionRisk(Double currentPollutionRisk) { this.currentPollutionRisk = currentPollutionRisk; }

    public Double getProjectedPollutionRisk() { return projectedPollutionRisk; }
    public void setProjectedPollutionRisk(Double projectedPollutionRisk) { this.projectedPollutionRisk = projectedPollutionRisk; }

    public Double getCurrentWaterRisk() { return currentWaterRisk; }
    public void setCurrentWaterRisk(Double currentWaterRisk) { this.currentWaterRisk = currentWaterRisk; }

    public Double getProjectedWaterRisk() { return projectedWaterRisk; }
    public void setProjectedWaterRisk(Double projectedWaterRisk) { this.projectedWaterRisk = projectedWaterRisk; }

    public Double getCurrentOverallRisk() { return currentOverallRisk; }
    public void setCurrentOverallRisk(Double currentOverallRisk) { this.currentOverallRisk = currentOverallRisk; }

    public Double getProjectedOverallRisk() { return projectedOverallRisk; }
    public void setProjectedOverallRisk(Double projectedOverallRisk) { this.projectedOverallRisk = projectedOverallRisk; }

    public Double getRiskReductionPct() { return riskReductionPct; }
    public void setRiskReductionPct(Double riskReductionPct) { this.riskReductionPct = riskReductionPct; }

    public Long getPopulationBenefited() { return populationBenefited; }
    public void setPopulationBenefited(Long populationBenefited) { this.populationBenefited = populationBenefited; }

    public Double getEstimatedCostInrCrores() { return estimatedCostInrCrores; }
    public void setEstimatedCostInrCrores(Double estimatedCostInrCrores) { this.estimatedCostInrCrores = estimatedCostInrCrores; }

    public Long getCo2ReductionTonsYr() { return co2ReductionTonsYr; }
    public void setCo2ReductionTonsYr(Long co2ReductionTonsYr) { this.co2ReductionTonsYr = co2ReductionTonsYr; }

    public Double getEstimatedRoi() { return estimatedRoi; }
    public void setEstimatedRoi(Double estimatedRoi) { this.estimatedRoi = estimatedRoi; }

    public String getScientificRationale() { return scientificRationale; }
    public void setScientificRationale(String scientificRationale) { this.scientificRationale = scientificRationale; }
}
