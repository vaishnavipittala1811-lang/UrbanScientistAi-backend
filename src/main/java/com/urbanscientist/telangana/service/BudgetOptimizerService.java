package com.urbanscientist.telangana.service;

import com.urbanscientist.telangana.dto.BudgetOptimizationRequest;
import com.urbanscientist.telangana.dto.BudgetOptimizationResult;
import com.urbanscientist.telangana.model.Intervention;
import com.urbanscientist.telangana.repository.InterventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetOptimizerService {

    @Autowired
    private InterventionRepository interventionRepository;

    public BudgetOptimizationResult optimizeBudget(BudgetOptimizationRequest request) {
        double budget = request.getBudgetInrCrores() != null && request.getBudgetInrCrores() > 0
                ? request.getBudgetInrCrores() : 150.0;

        List<Intervention> pool = interventionRepository.findAll();
        if (request.getSelectedInterventionIds() != null && !request.getSelectedInterventionIds().isEmpty()) {
            pool = pool.stream()
                    .filter(i -> request.getSelectedInterventionIds().contains(i.getInterventionId()))
                    .collect(Collectors.toList());
        }

        // Sort candidates by cost-benefit efficiency: (riskReduction * populationBenefited) / cost
        pool.sort(Comparator.comparingDouble((Intervention i) -> {
            double cost = i.getCostInrCrores() != null && i.getCostInrCrores() > 0 ? i.getCostInrCrores() : 1.0;
            double reduction = i.getExpectedRiskReductionPct() != null ? i.getExpectedRiskReductionPct() : 10.0;
            long pop = i.getEstPopulationBenefited() != null ? i.getEstPopulationBenefited() : 100000L;
            return (reduction * (pop / 10000.0)) / cost;
        }).reversed());

        double budgetUsed = 0.0;
        List<Intervention> selected = new ArrayList<>();
        double totalRiskRed = 0.0;
        long totalPop = 0L;
        long totalCo2 = 0L;
        double roiSum = 0.0;

        for (Intervention candidate : pool) {
            double cost = candidate.getCostInrCrores() != null ? candidate.getCostInrCrores() : 0.0;
            if (budgetUsed + cost <= budget) {
                selected.add(candidate);
                budgetUsed += cost;
                totalRiskRed += (candidate.getExpectedRiskReductionPct() != null ? candidate.getExpectedRiskReductionPct() : 0.0);
                totalPop += (candidate.getEstPopulationBenefited() != null ? candidate.getEstPopulationBenefited() : 0L);
                totalCo2 += (candidate.getCo2ReductionTonsYr() != null ? candidate.getCo2ReductionTonsYr() : 0L);
                roiSum += (candidate.getRoiMultiplier() != null ? candidate.getRoiMultiplier() : 1.0);
            }
        }

        budgetUsed = Math.round(budgetUsed * 100.0) / 100.0;
        double remaining = Math.round((budget - budgetUsed) * 100.0) / 100.0;
        double avgRoi = selected.isEmpty() ? 0.0 : Math.round((roiSum / selected.size()) * 10.0) / 10.0;
        double netRiskReduction = Math.min(68.0, Math.round(totalRiskRed * 0.75 * 10.0) / 10.0);
        double impactScore = Math.min(99.0, Math.round(((totalPop / 50000.0) * 0.4 + netRiskReduction * 0.4 + avgRoi * 5.0) * 10.0) / 10.0);

        BudgetOptimizationResult result = new BudgetOptimizationResult();
        result.setGeographicScope("Telangana, India");
        result.setDistrict(request.getDistrict() != null ? request.getDistrict() : "All Telangana");
        result.setTotalBudgetInrCrores(budget);
        result.setBudgetUsedInrCrores(budgetUsed);
        result.setRemainingBudgetInrCrores(remaining);
        result.setRecommendedInterventions(selected);
        result.setTotalRiskReductionPct(netRiskReduction);
        result.setTotalPopulationImpacted(totalPop);
        result.setTotalCo2ReductionTons(totalCo2);
        result.setAverageRoiMultiplier(avgRoi);
        result.setOverallImpactScore(impactScore);

        return result;
    }
}
