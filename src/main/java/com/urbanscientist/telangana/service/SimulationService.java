package com.urbanscientist.telangana.service;

import com.urbanscientist.telangana.dto.SimulationRequest;
import com.urbanscientist.telangana.dto.SimulationResult;
import com.urbanscientist.telangana.model.*;
import com.urbanscientist.telangana.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {

    @Autowired
    private UrbanLocationRepository locationRepository;

    @Autowired
    private TrafficRepository trafficRepository;

    @Autowired
    private PollutionRepository pollutionRepository;

    @Autowired
    private WaterRepository waterRepository;

    @Autowired
    private HealthcareRepository healthcareRepository;

    @Autowired
    private InfrastructureRepository infrastructureRepository;

    @Autowired
    private PopulationRepository populationRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    public SimulationResult runSimulation(SimulationRequest request) {
        // Find matching location or default to first in district/all
        UrbanLocation location = null;
        if (request.getZone() != null && !request.getZone().isBlank()) {
            List<UrbanLocation> locs = locationRepository.findAll();
            location = locs.stream()
                    .filter(l -> l.getZone().equalsIgnoreCase(request.getZone()) || l.getLocationName().contains(request.getZone()))
                    .findFirst().orElse(null);
        }
        if (location == null && request.getCity() != null && !request.getCity().isBlank()) {
            List<UrbanLocation> locs = locationRepository.findByCityIgnoreCase(request.getCity());
            if (!locs.isEmpty()) location = locs.get(0);
        }
        if (location == null && request.getDistrict() != null && !request.getDistrict().isBlank()) {
            List<UrbanLocation> locs = locationRepository.findByDistrictIgnoreCase(request.getDistrict());
            if (!locs.isEmpty()) location = locs.get(0);
        }
        if (location == null) {
            List<UrbanLocation> all = locationRepository.findAll();
            location = all.isEmpty() ? null : all.get(0);
        }

        // Fetch intervention
        Intervention intervention = null;
        if (request.getInterventionId() != null && !request.getInterventionId().isBlank()) {
            intervention = interventionRepository.findByInterventionId(request.getInterventionId()).orElse(null);
        }
        if (intervention == null) {
            List<Intervention> allInt = interventionRepository.findAll();
            intervention = allInt.isEmpty() ? null : allInt.get(0);
        }

        String locId = location != null ? location.getLocationId() : "TS-LOC-001";
        String distName = location != null ? location.getDistrict() : "Hyderabad";
        String locName = location != null ? location.getLocationName() : "Central Zone, Hyderabad";

        TrafficRecord traffic = trafficRepository.findByLocationId(locId).orElse(null);
        PollutionRecord pollution = pollutionRepository.findByLocationId(locId).orElse(null);
        WaterRecord water = waterRepository.findByLocationId(locId).orElse(null);
        HealthcareRecord healthcare = healthcareRepository.findByLocationId(locId).orElse(null);
        InfrastructureRecord infra = infrastructureRepository.findByLocationId(locId).orElse(null);
        PopulationRecord pop = populationRepository.findByLocationId(locId).orElse(null);

        double curTraffic = traffic != null && traffic.getTrafficRiskScore() != null ? traffic.getTrafficRiskScore() : 68.0;
        double curPollution = pollution != null && pollution.getPollutionRiskScore() != null ? pollution.getPollutionRiskScore() : 64.0;
        double curWater = water != null && water.getWaterRiskScore() != null ? water.getWaterRiskScore() : 58.0;
        double curHc = healthcare != null && healthcare.getHealthcarePressureScore() != null ? healthcare.getHealthcarePressureScore() : 52.0;
        double curInfra = infra != null && infra.getInfrastructureStressScore() != null ? infra.getInfrastructureStressScore() : 55.0;

        double curOverall = Math.round((curTraffic * 0.22 + curPollution * 0.25 + curWater * 0.20 + curHc * 0.18 + curInfra * 0.15) * 10.0) / 10.0;

        double reductionFactor = intervention != null ? (intervention.getExpectedRiskReductionPct() / 100.0) : 0.25;
        String cat = intervention != null ? intervention.getCategory() : "Traffic";

        // Multi-domain dynamic cross-sector calculations
        double projTraffic = curTraffic;
        double projPollution = curPollution;
        double projWater = curWater;
        double projHc = curHc;
        double projInfra = curInfra;

        StringBuilder rationale = new StringBuilder();
        rationale.append("Backend simulation executed for ").append(locName).append(" in Telangana. ");

        if ("Traffic".equalsIgnoreCase(cat)) {
            projTraffic = Math.max(15.0, curTraffic * (1.0 - reductionFactor));
            projPollution = Math.max(18.0, curPollution * (1.0 - reductionFactor * 0.35)); // Idle emissions reduction
            rationale.append(String.format("Adaptive signal synchronization and mobility flow optimization mitigates arterial bottlenecks by %.1f%%, resulting in a secondary 8-12%% drop in vehicular ambient NO2 and PM2.5 levels.", reductionFactor * 100));
        } else if ("Pollution".equalsIgnoreCase(cat)) {
            projPollution = Math.max(15.0, curPollution * (1.0 - reductionFactor));
            projWater = Math.max(20.0, curWater * 0.95);
            rationale.append(String.format("Urban micro-forest canopy and industrial dust misting reduces particulate matter and mitigates local surface urban heat island (UHI) intensity by ~1.3°C, easing thermal runoff stress.", reductionFactor * 100));
        } else if ("Water".equalsIgnoreCase(cat)) {
            projWater = Math.max(12.0, curWater * (1.0 - reductionFactor));
            projInfra = Math.max(15.0, curInfra * 0.93);
            rationale.append(String.format("Telemetry-driven pressure management and smart metering cuts non-revenue water loss by %.1f%% across the municipal distribution network, improving equitable supply duration.", reductionFactor * 100));
        } else if ("Healthcare".equalsIgnoreCase(cat)) {
            projHc = Math.max(15.0, curHc * (1.0 - reductionFactor));
            rationale.append(String.format("Decentralized Basti Dawakhana tele-clinics relieve secondary teaching hospital outpatient surges, compressing ambulance transit queue times by ~32%%.", reductionFactor * 100));
        } else if ("Infrastructure".equalsIgnoreCase(cat)) {
            projInfra = Math.max(15.0, curInfra * (1.0 - reductionFactor));
            projWater = Math.max(15.0, curWater * 0.90);
            rationale.append(String.format("Sponge city permeable retrofits and restored storm drainage conduits reduce localized monsoon inundation risk by %.1f%%.", reductionFactor * 100));
        } else {
            projTraffic = curTraffic * 0.85;
            projPollution = curPollution * 0.88;
        }

        double projOverall = Math.round((projTraffic * 0.22 + projPollution * 0.25 + projWater * 0.20 + projHc * 0.18 + projInfra * 0.15) * 10.0) / 10.0;
        double overallReductionPct = Math.round(((curOverall - projOverall) / curOverall * 100.0) * 10.0) / 10.0;

        long popBase = pop != null && pop.getCurrentPopulation() != null ? pop.getCurrentPopulation() : 180000L;
        long benefited = Math.round(popBase * (intervention != null && "District".equalsIgnoreCase(intervention.getApplicableScope()) ? 1.8 : 0.85));

        SimulationResult result = new SimulationResult();
        result.setGeographicScope("Telangana, India");
        result.setLocation(locName);
        result.setDistrict(distName);
        result.setInterventionName(intervention != null ? intervention.getName() : "Urban Intervention");
        result.setCategory(cat);
        result.setCurrentTrafficRisk(Math.round(curTraffic * 10.0) / 10.0);
        result.setProjectedTrafficRisk(Math.round(projTraffic * 10.0) / 10.0);
        result.setCurrentPollutionRisk(Math.round(curPollution * 10.0) / 10.0);
        result.setProjectedPollutionRisk(Math.round(projPollution * 10.0) / 10.0);
        result.setCurrentWaterRisk(Math.round(curWater * 10.0) / 10.0);
        result.setProjectedWaterRisk(Math.round(projWater * 10.0) / 10.0);
        result.setCurrentOverallRisk(curOverall);
        result.setProjectedOverallRisk(projOverall);
        result.setRiskReductionPct(overallReductionPct);
        result.setPopulationBenefited(benefited);
        result.setEstimatedCostInrCrores(intervention != null ? intervention.getCostInrCrores() : 35.0);
        result.setCo2ReductionTonsYr(intervention != null ? intervention.getCo2ReductionTonsYr() : 12000L);
        result.setEstimatedRoi(intervention != null ? intervention.getRoiMultiplier() : 3.2);
        result.setScientificRationale(rationale.toString());

        return result;
    }
}
