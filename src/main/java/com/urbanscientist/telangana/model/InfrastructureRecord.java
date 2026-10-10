package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "infrastructure_records", indexes = {
    @Index(name = "idx_inf_loc", columnList = "locationId"),
    @Index(name = "idx_inf_district", columnList = "district")
})
public class InfrastructureRecord {

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

    private Double powerGridReliabilityPct;
    private Double sewageNetworkCoveragePct;
    private Double solidWasteCollectedTpd;
    private Double wasteProcessingEfficiencyPct;
    private Double greenCoverRatioPct;
    private Double roadQualityPci;
    private Double infrastructureStressScore;
    private String repairPriorityRank;
    private String dataType = "SYNTHETIC_DEMO";

    public InfrastructureRecord() {}

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

    public Double getPowerGridReliabilityPct() { return powerGridReliabilityPct; }
    public void setPowerGridReliabilityPct(Double powerGridReliabilityPct) { this.powerGridReliabilityPct = powerGridReliabilityPct; }

    public Double getSewageNetworkCoveragePct() { return sewageNetworkCoveragePct; }
    public void setSewageNetworkCoveragePct(Double sewageNetworkCoveragePct) { this.sewageNetworkCoveragePct = sewageNetworkCoveragePct; }

    public Double getSolidWasteCollectedTpd() { return solidWasteCollectedTpd; }
    public void setSolidWasteCollectedTpd(Double solidWasteCollectedTpd) { this.solidWasteCollectedTpd = solidWasteCollectedTpd; }

    public Double getWasteProcessingEfficiencyPct() { return wasteProcessingEfficiencyPct; }
    public void setWasteProcessingEfficiencyPct(Double wasteProcessingEfficiencyPct) { this.wasteProcessingEfficiencyPct = wasteProcessingEfficiencyPct; }

    public Double getGreenCoverRatioPct() { return greenCoverRatioPct; }
    public void setGreenCoverRatioPct(Double greenCoverRatioPct) { this.greenCoverRatioPct = greenCoverRatioPct; }

    public Double getRoadQualityPci() { return roadQualityPci; }
    public void setRoadQualityPci(Double roadQualityPci) { this.roadQualityPci = roadQualityPci; }

    public Double getInfrastructureStressScore() { return infrastructureStressScore; }
    public void setInfrastructureStressScore(Double infrastructureStressScore) { this.infrastructureStressScore = infrastructureStressScore; }

    public String getRepairPriorityRank() { return repairPriorityRank; }
    public void setRepairPriorityRank(String repairPriorityRank) { this.repairPriorityRank = repairPriorityRank; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
