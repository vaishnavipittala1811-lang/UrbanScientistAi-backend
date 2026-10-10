package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "interventions")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String interventionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Double costInrCrores;

    @Column(nullable = false)
    private Integer implementationTimeMonths;

    @Column(nullable = false)
    private Double expectedRiskReductionPct;

    private Long co2ReductionTonsYr;
    private Long estPopulationBenefited;
    private Double roiMultiplier;
    private String priorityLevel;
    private String applicableScope;
    private String dataType = "SYNTHETIC_DEMO";

    public Intervention() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInterventionId() { return interventionId; }
    public void setInterventionId(String interventionId) { this.interventionId = interventionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getCostInrCrores() { return costInrCrores; }
    public void setCostInrCrores(Double costInrCrores) { this.costInrCrores = costInrCrores; }

    public Integer getImplementationTimeMonths() { return implementationTimeMonths; }
    public void setImplementationTimeMonths(Integer implementationTimeMonths) { this.implementationTimeMonths = implementationTimeMonths; }

    public Double getExpectedRiskReductionPct() { return expectedRiskReductionPct; }
    public void setExpectedRiskReductionPct(Double expectedRiskReductionPct) { this.expectedRiskReductionPct = expectedRiskReductionPct; }

    public Long getCo2ReductionTonsYr() { return co2ReductionTonsYr; }
    public void setCo2ReductionTonsYr(Long co2ReductionTonsYr) { this.co2ReductionTonsYr = co2ReductionTonsYr; }

    public Long getEstPopulationBenefited() { return estPopulationBenefited; }
    public void setEstPopulationBenefited(Long estPopulationBenefited) { this.estPopulationBenefited = estPopulationBenefited; }

    public Double getRoiMultiplier() { return roiMultiplier; }
    public void setRoiMultiplier(Double roiMultiplier) { this.roiMultiplier = roiMultiplier; }

    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }

    public String getApplicableScope() { return applicableScope; }
    public void setApplicableScope(String applicableScope) { this.applicableScope = applicableScope; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
