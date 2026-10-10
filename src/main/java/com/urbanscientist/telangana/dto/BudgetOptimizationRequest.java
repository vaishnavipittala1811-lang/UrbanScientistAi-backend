package com.urbanscientist.telangana.dto;

import java.util.List;

public class BudgetOptimizationRequest {

    private String district;
    private String city;
    private Double budgetInrCrores;
    private List<String> selectedInterventionIds;

    public BudgetOptimizationRequest() {}

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Double getBudgetInrCrores() { return budgetInrCrores; }
    public void setBudgetInrCrores(Double budgetInrCrores) { this.budgetInrCrores = budgetInrCrores; }

    public List<String> getSelectedInterventionIds() { return selectedInterventionIds; }
    public void setSelectedInterventionIds(List<String> selectedInterventionIds) { this.selectedInterventionIds = selectedInterventionIds; }
}
