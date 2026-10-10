package com.urbanscientist.telangana.dto;

import com.urbanscientist.telangana.model.Intervention;
import java.util.List;

public class BudgetOptimizationResult {

    private String geographicScope = "Telangana, India";
    private String district;
    private Double totalBudgetInrCrores;
    private Double budgetUsedInrCrores;
    private Double remainingBudgetInrCrores;
    private List<Intervention> recommendedInterventions;
    private Double totalRiskReductionPct;
    private Long totalPopulationImpacted;
    private Long totalCo2ReductionTons;
    private Double averageRoiMultiplier;
    private Double overallImpactScore;

    public BudgetOptimizationResult() {}

    public String getGeographicScope() { return geographicScope; }
    public void setGeographicScope(String geographicScope) { this.geographicScope = geographicScope; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public Double getTotalBudgetInrCrores() { return totalBudgetInrCrores; }
    public void setTotalBudgetInrCrores(Double totalBudgetInrCrores) { this.totalBudgetInrCrores = totalBudgetInrCrores; }

    public Double getBudgetUsedInrCrores() { return budgetUsedInrCrores; }
    public void setBudgetUsedInrCrores(Double budgetUsedInrCrores) { this.budgetUsedInrCrores = budgetUsedInrCrores; }

    public Double getRemainingBudgetInrCrores() { return remainingBudgetInrCrores; }
    public void setRemainingBudgetInrCrores(Double remainingBudgetInrCrores) { this.remainingBudgetInrCrores = remainingBudgetInrCrores; }

    public List<Intervention> getRecommendedInterventions() { return recommendedInterventions; }
    public void setRecommendedInterventions(List<Intervention> recommendedInterventions) { this.recommendedInterventions = recommendedInterventions; }

    public Double getTotalRiskReductionPct() { return totalRiskReductionPct; }
    public void setTotalRiskReductionPct(Double totalRiskReductionPct) { this.totalRiskReductionPct = totalRiskReductionPct; }

    public Long getTotalPopulationImpacted() { return totalPopulationImpacted; }
    public void setTotalPopulationImpacted(Long totalPopulationImpacted) { this.totalPopulationImpacted = totalPopulationImpacted; }

    public Long getTotalCo2ReductionTons() { return totalCo2ReductionTons; }
    public void setTotalCo2ReductionTons(Long totalCo2ReductionTons) { this.totalCo2ReductionTons = totalCo2ReductionTons; }

    public Double getAverageRoiMultiplier() { return averageRoiMultiplier; }
    public void setAverageRoiMultiplier(Double averageRoiMultiplier) { this.averageRoiMultiplier = averageRoiMultiplier; }

    public Double getOverallImpactScore() { return overallImpactScore; }
    public void setOverallImpactScore(Double overallImpactScore) { this.overallImpactScore = overallImpactScore; }
}
