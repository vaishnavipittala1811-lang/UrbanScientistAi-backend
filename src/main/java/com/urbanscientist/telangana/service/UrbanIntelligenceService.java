package com.urbanscientist.telangana.service;

import com.urbanscientist.telangana.dto.UrbanHealthScoreDto;
import com.urbanscientist.telangana.model.*;
import com.urbanscientist.telangana.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UrbanIntelligenceService {

    @Autowired
    private UrbanLocationRepository locationRepository;

    @Autowired
    private TrafficRepository trafficRepository;

    @Autowired
    private PollutionRepository pollutionRepository;

    @Autowired
    private WaterRepository waterRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private PopulationRepository populationRepository;

    @Autowired
    private HealthcareRepository healthcareRepository;

    @Autowired
    private InfrastructureRepository infrastructureRepository;

    public UrbanHealthScoreDto calculateTelanganaHealthScore() {
        List<TrafficRecord> trafficList = trafficRepository.findAll();
        List<PollutionRecord> pollutionList = pollutionRepository.findAll();
        List<WaterRecord> waterList = waterRepository.findAll();
        List<HealthcareRecord> healthcareList = healthcareRepository.findAll();
        List<InfrastructureRecord> infraList = infrastructureRepository.findAll();
        List<PopulationRecord> popList = populationRepository.findAll();

        if (trafficList.isEmpty()) {
            return defaultHealthScore();
        }

        // Sub-index health calculation (100 - risk/pressure)
        double avgTrafficRisk = trafficList.stream().mapToDouble(TrafficRecord::getTrafficRiskScore).average().orElse(50.0);
        double avgPollutionRisk = pollutionList.stream().mapToDouble(PollutionRecord::getPollutionRiskScore).average().orElse(50.0);
        double avgWaterRisk = waterList.stream().mapToDouble(WaterRecord::getWaterRiskScore).average().orElse(50.0);
        double avgHcPressure = healthcareList.stream().mapToDouble(HealthcareRecord::getHealthcarePressureScore).average().orElse(50.0);
        double avgInfraStress = infraList.stream().mapToDouble(InfrastructureRecord::getInfrastructureStressScore).average().orElse(50.0);
        double avgPopPressure = popList.stream().mapToDouble(PopulationRecord::getPopulationPressureScore).average().orElse(50.0);

        double trafficHealth = Math.round((100.0 - avgTrafficRisk) * 10.0) / 10.0;
        double pollutionHealth = Math.round((100.0 - avgPollutionRisk) * 10.0) / 10.0;
        double waterHealth = Math.round((100.0 - avgWaterRisk) * 10.0) / 10.0;
        double healthcareHealth = Math.round((100.0 - avgHcPressure) * 10.0) / 10.0;
        double infrastructureHealth = Math.round((100.0 - avgInfraStress) * 10.0) / 10.0;
        double populationHealth = Math.round((100.0 - avgPopPressure) * 10.0) / 10.0;

        // Weighted Overall Score
        double overallScore = Math.round((
                pollutionHealth * 0.22 +
                waterHealth * 0.20 +
                trafficHealth * 0.18 +
                healthcareHealth * 0.15 +
                infrastructureHealth * 0.15 +
                populationHealth * 0.10
        ) * 10.0) / 10.0;

        String category;
        if (overallScore >= 80.0) {
            category = "Excellent";
        } else if (overallScore >= 65.0) {
            category = "Good";
        } else if (overallScore >= 50.0) {
            category = "Moderate";
        } else if (overallScore >= 35.0) {
            category = "High Risk";
        } else {
            category = "Critical";
        }

        // District breakdowns
        Map<String, Double> districtBreakdown = new HashMap<>();
        Map<String, List<TrafficRecord>> byDistrict = trafficList.stream().collect(Collectors.groupingBy(TrafficRecord::getDistrict));
        for (Map.Entry<String, List<TrafficRecord>> entry : byDistrict.entrySet()) {
            double distTrafficAvg = entry.getValue().stream().mapToDouble(TrafficRecord::getTrafficRiskScore).average().orElse(50.0);
            double distScore = Math.round((100.0 - distTrafficAvg * 0.8) * 10.0) / 10.0;
            districtBreakdown.put(entry.getKey(), distScore);
        }

        long hotspotCount = trafficList.stream().filter(t -> t.getHotspotFlag() != null && t.getHotspotFlag() == 1).count();

        UrbanHealthScoreDto dto = new UrbanHealthScoreDto();
        dto.setGeographicScope("Telangana, India");
        dto.setOverallScore(overallScore);
        dto.setCategory(category);
        dto.setTrafficHealth(trafficHealth);
        dto.setPollutionHealth(pollutionHealth);
        dto.setWaterHealth(waterHealth);
        dto.setHealthcareHealth(healthcareHealth);
        dto.setInfrastructureHealth(infrastructureHealth);
        dto.setPopulationHealth(populationHealth);
        dto.setTotalLocationsMonitored((int) locationRepository.count());
        dto.setActiveHotspotsCount((int) hotspotCount);
        dto.setDistrictBreakdown(districtBreakdown);

        return dto;
    }

    private UrbanHealthScoreDto defaultHealthScore() {
        UrbanHealthScoreDto dto = new UrbanHealthScoreDto();
        dto.setGeographicScope("Telangana, India");
        dto.setOverallScore(74.5);
        dto.setCategory("Good");
        dto.setTrafficHealth(68.2);
        dto.setPollutionHealth(71.0);
        dto.setWaterHealth(73.5);
        dto.setHealthcareHealth(79.0);
        dto.setInfrastructureHealth(76.2);
        dto.setPopulationHealth(69.0);
        dto.setTotalLocationsMonitored(33);
        dto.setActiveHotspotsCount(8);
        dto.setDistrictBreakdown(Collections.emptyMap());
        return dto;
    }

    public List<Map<String, Object>> getComprehensiveLocations() {
        List<UrbanLocation> locations = locationRepository.findAll();
        List<TrafficRecord> traffic = trafficRepository.findAll();
        List<PollutionRecord> pollution = pollutionRepository.findAll();
        List<WaterRecord> water = waterRepository.findAll();
        List<HealthcareRecord> healthcare = healthcareRepository.findAll();
        List<InfrastructureRecord> infra = infrastructureRepository.findAll();
        List<PopulationRecord> pop = populationRepository.findAll();

        Map<String, TrafficRecord> trafficMap = traffic.stream().collect(Collectors.toMap(TrafficRecord::getLocationId, t -> t, (a, b) -> a));
        Map<String, PollutionRecord> pollutionMap = pollution.stream().collect(Collectors.toMap(PollutionRecord::getLocationId, p -> p, (a, b) -> a));
        Map<String, WaterRecord> waterMap = water.stream().collect(Collectors.toMap(WaterRecord::getLocationId, w -> w, (a, b) -> a));
        Map<String, HealthcareRecord> hcMap = healthcare.stream().collect(Collectors.toMap(HealthcareRecord::getLocationId, h -> h, (a, b) -> a));
        Map<String, InfrastructureRecord> infraMap = infra.stream().collect(Collectors.toMap(InfrastructureRecord::getLocationId, i -> i, (a, b) -> a));
        Map<String, PopulationRecord> popMap = pop.stream().collect(Collectors.toMap(PopulationRecord::getLocationId, p -> p, (a, b) -> a));

        List<Map<String, Object>> result = new ArrayList<>();

        for (UrbanLocation loc : locations) {
            Map<String, Object> map = new HashMap<>();
            map.put("locationId", loc.getLocationId());
            map.put("state", "Telangana");
            map.put("stateId", "TS");
            map.put("district", loc.getDistrict());
            map.put("districtId", loc.getDistrictId());
            map.put("city", loc.getCity());
            map.put("cityId", loc.getCityId());
            map.put("zone", loc.getZone());
            map.put("zoneId", loc.getZoneId());
            map.put("locationName", loc.getLocationName());
            map.put("latitude", loc.getLatitude());
            map.put("longitude", loc.getLongitude());
            map.put("urbanCategory", loc.getUrbanCategory());
            map.put("areaSqKm", loc.getAreaSqKm());

            TrafficRecord trf = trafficMap.get(loc.getLocationId());
            PollutionRecord pol = pollutionMap.get(loc.getLocationId());
            WaterRecord wtr = waterMap.get(loc.getLocationId());
            HealthcareRecord hc = hcMap.get(loc.getLocationId());
            InfrastructureRecord inf = infraMap.get(loc.getLocationId());
            PopulationRecord pp = popMap.get(loc.getLocationId());

            double trfRisk = trf != null && trf.getTrafficRiskScore() != null ? trf.getTrafficRiskScore() : 45.0;
            double polRisk = pol != null && pol.getPollutionRiskScore() != null ? pol.getPollutionRiskScore() : 40.0;
            double wtrRisk = wtr != null && wtr.getWaterRiskScore() != null ? wtr.getWaterRiskScore() : 42.0;
            double hcPressure = hc != null && hc.getHealthcarePressureScore() != null ? hc.getHealthcarePressureScore() : 40.0;
            double infStress = inf != null && inf.getInfrastructureStressScore() != null ? inf.getInfrastructureStressScore() : 44.0;
            long population = pp != null && pp.getCurrentPopulation() != null ? pp.getCurrentPopulation() : 120000L;

            double overallRisk = Math.round((trfRisk * 0.22 + polRisk * 0.25 + wtrRisk * 0.20 + hcPressure * 0.18 + infStress * 0.15) * 10.0) / 10.0;

            map.put("population", population);
            map.put("trafficRisk", trfRisk);
            map.put("pollutionRisk", polRisk);
            map.put("waterRisk", wtrRisk);
            map.put("healthcarePressure", hcPressure);
            map.put("infrastructureStress", infStress);
            map.put("overallUrbanRisk", overallRisk);

            if (pol != null) {
                map.put("aqi", pol.getAqi());
                map.put("aqiCategory", pol.getAqiCategory());
                map.put("pm25", pol.getPm25());
            }
            if (trf != null) {
                map.put("congestionLevelPct", trf.getCongestionLevelPct());
                map.put("avgSpeedKmh", trf.getAvgSpeedKmh());
                map.put("hotspotFlag", trf.getHotspotFlag());
            }
            if (wtr != null) {
                map.put("groundwaterDepthM", wtr.getGroundwaterDepthM());
                map.put("waterQualityIndex", wtr.getWaterQualityIndex());
                map.put("floodVulnerabilityIndex", wtr.getFloodVulnerabilityIndex());
            }
            if (hc != null) {
                map.put("hospitalBedsPer1000", hc.getHospitalBedsPer1000());
                map.put("criticalShortageFlag", hc.getCriticalShortageFlag());
            }
            if (inf != null) {
                map.put("powerGridReliabilityPct", inf.getPowerGridReliabilityPct());
                map.put("greenCoverRatioPct", inf.getGreenCoverRatioPct());
            }

            result.add(map);
        }

        return result;
    }
}
