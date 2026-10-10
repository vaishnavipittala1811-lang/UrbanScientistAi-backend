package com.urbanscientist.telangana.controller;

import com.urbanscientist.telangana.model.*;
import com.urbanscientist.telangana.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SectorControllers {

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

    @GetMapping("/traffic")
    public ResponseEntity<Map<String, Object>> getTrafficData(@RequestParam(required = false) String district) {
        List<TrafficRecord> list = (district != null && !district.isBlank())
                ? trafficRepository.findByDistrictIgnoreCase(district)
                : trafficRepository.findAll();

        double avgCongestion = list.stream().mapToDouble(t -> t.getCongestionLevelPct() != null ? t.getCongestionLevelPct() : 0.0).average().orElse(0.0);
        double avgSpeed = list.stream().mapToDouble(t -> t.getAvgSpeedKmh() != null ? t.getAvgSpeedKmh() : 0.0).average().orElse(0.0);
        long hotspots = list.stream().filter(t -> t.getHotspotFlag() != null && t.getHotspotFlag() == 1).count();

        Map<String, Object> res = new HashMap<>();
        res.put("state", "Telangana");
        res.put("records", list);
        res.put("avgCongestionPct", Math.round(avgCongestion * 10.0) / 10.0);
        res.put("avgSpeedKmh", Math.round(avgSpeed * 10.0) / 10.0);
        res.put("hotspotCount", hotspots);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/pollution")
    public ResponseEntity<Map<String, Object>> getPollutionData(@RequestParam(required = false) String district) {
        List<PollutionRecord> list = (district != null && !district.isBlank())
                ? pollutionRepository.findByDistrictIgnoreCase(district)
                : pollutionRepository.findAll();

        double avgAqi = list.stream().mapToInt(p -> p.getAqi() != null ? p.getAqi() : 0).average().orElse(0.0);
        double avgPm25 = list.stream().mapToDouble(p -> p.getPm25() != null ? p.getPm25() : 0.0).average().orElse(0.0);
        long criticalCount = list.stream().filter(p -> p.getAqi() != null && p.getAqi() > 200).count();

        Map<String, Object> res = new HashMap<>();
        res.put("state", "Telangana");
        res.put("records", list);
        res.put("avgAqi", Math.round(avgAqi));
        res.put("avgPm25", Math.round(avgPm25 * 10.0) / 10.0);
        res.put("criticalAqiCount", criticalCount);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/water")
    public ResponseEntity<Map<String, Object>> getWaterData(@RequestParam(required = false) String district) {
        List<WaterRecord> list = (district != null && !district.isBlank())
                ? waterRepository.findByDistrictIgnoreCase(district)
                : waterRepository.findAll();

        double avgDepth = list.stream().mapToDouble(w -> w.getGroundwaterDepthM() != null ? w.getGroundwaterDepthM() : 0.0).average().orElse(0.0);
        double avgDeficit = list.stream().mapToDouble(w -> w.getSupplyDeficitPct() != null ? w.getSupplyDeficitPct() : 0.0).average().orElse(0.0);
        double avgWqi = list.stream().mapToDouble(w -> w.getWaterQualityIndex() != null ? w.getWaterQualityIndex() : 0.0).average().orElse(0.0);

        Map<String, Object> res = new HashMap<>();
        res.put("state", "Telangana");
        res.put("records", list);
        res.put("avgGroundwaterDepthM", Math.round(avgDepth * 10.0) / 10.0);
        res.put("avgSupplyDeficitPct", Math.round(avgDeficit * 10.0) / 10.0);
        res.put("avgWaterQualityIndex", Math.round(avgWqi * 10.0) / 10.0);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/weather")
    public ResponseEntity<Map<String, Object>> getWeatherData(@RequestParam(required = false) String district) {
        List<WeatherRecord> list = (district != null && !district.isBlank())
                ? weatherRepository.findByDistrictIgnoreCase(district)
                : weatherRepository.findAll();

        double avgTemp = list.stream().mapToDouble(w -> w.getTemperatureC() != null ? w.getTemperatureC() : 0.0).average().orElse(0.0);
        double avgRain = list.stream().mapToDouble(w -> w.getRainfallMmMonthly() != null ? w.getRainfallMmMonthly() : 0.0).average().orElse(0.0);
        double avgUhi = list.stream().mapToDouble(w -> w.getSurfaceHeatIslandIntensityC() != null ? w.getSurfaceHeatIslandIntensityC() : 0.0).average().orElse(0.0);

        Map<String, Object> res = new HashMap<>();
        res.put("state", "Telangana");
        res.put("records", list);
        res.put("avgTemperatureC", Math.round(avgTemp * 10.0) / 10.0);
        res.put("avgRainfallMm", Math.round(avgRain * 10.0) / 10.0);
        res.put("avgUhiIntensityC", Math.round(avgUhi * 10.0) / 10.0);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/population")
    public ResponseEntity<Map<String, Object>> getPopulationData(@RequestParam(required = false) String district) {
        List<PopulationRecord> list = (district != null && !district.isBlank())
                ? populationRepository.findByDistrictIgnoreCase(district)
                : populationRepository.findAll();

        long totalCurrent = list.stream().mapToLong(p -> p.getCurrentPopulation() != null ? p.getCurrentPopulation() : 0L).sum();
        long total5yr = list.stream().mapToLong(p -> p.getForecast5yr() != null ? p.getForecast5yr() : 0L).sum();
        long total10yr = list.stream().mapToLong(p -> p.getForecast10yr() != null ? p.getForecast10yr() : 0L).sum();
        long total15yr = list.stream().mapToLong(p -> p.getForecast15yr() != null ? p.getForecast15yr() : 0L).sum();
        double avgDensity = list.stream().mapToInt(p -> p.getPopulationDensityPerSqkm() != null ? p.getPopulationDensityPerSqkm() : 0).average().orElse(0.0);

        Map<String, Object> res = new HashMap<>();
        res.put("state", "Telangana");
        res.put("records", list);
        res.put("totalCurrentPopulation", totalCurrent);
        res.put("total5yrForecast", total5yr);
        res.put("total10yrForecast", total10yr);
        res.put("total15yrForecast", total15yr);
        res.put("avgDensityPerSqkm", Math.round(avgDensity));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/healthcare")
    public ResponseEntity<Map<String, Object>> getHealthcareData(@RequestParam(required = false) String district) {
        List<HealthcareRecord> list = (district != null && !district.isBlank())
                ? healthcareRepository.findByDistrictIgnoreCase(district)
                : healthcareRepository.findAll();

        double avgBeds = list.stream().mapToDouble(h -> h.getHospitalBedsPer1000() != null ? h.getHospitalBedsPer1000() : 0.0).average().orElse(0.0);
        double avgIcu = list.stream().mapToDouble(h -> h.getIcuOccupancyRatePct() != null ? h.getIcuOccupancyRatePct() : 0.0).average().orElse(0.0);
        double avgAmb = list.stream().mapToDouble(h -> h.getAmbulanceAvgResponseMin() != null ? h.getAmbulanceAvgResponseMin() : 0.0).average().orElse(0.0);
        long shortages = list.stream().filter(h -> h.getCriticalShortageFlag() != null && h.getCriticalShortageFlag() == 1).count();

        Map<String, Object> res = new HashMap<>();
        res.put("state", "Telangana");
        res.put("records", list);
        res.put("avgBedsPer1000", Math.round(avgBeds * 10.0) / 10.0);
        res.put("avgIcuOccupancyPct", Math.round(avgIcu * 10.0) / 10.0);
        res.put("avgAmbulanceResponseMin", Math.round(avgAmb * 10.0) / 10.0);
        res.put("criticalShortageCount", shortages);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/infrastructure")
    public ResponseEntity<Map<String, Object>> getInfrastructureData(@RequestParam(required = false) String district) {
        List<InfrastructureRecord> list = (district != null && !district.isBlank())
                ? infrastructureRepository.findByDistrictIgnoreCase(district)
                : infrastructureRepository.findAll();

        double avgPower = list.stream().mapToDouble(i -> i.getPowerGridReliabilityPct() != null ? i.getPowerGridReliabilityPct() : 0.0).average().orElse(0.0);
        double avgSewage = list.stream().mapToDouble(i -> i.getSewageNetworkCoveragePct() != null ? i.getSewageNetworkCoveragePct() : 0.0).average().orElse(0.0);
        double avgPci = list.stream().mapToDouble(i -> i.getRoadQualityPci() != null ? i.getRoadQualityPci() : 0.0).average().orElse(0.0);

        Map<String, Object> res = new HashMap<>();
        res.put("state", "Telangana");
        res.put("records", list);
        res.put("avgPowerGridReliabilityPct", Math.round(avgPower * 10.0) / 10.0);
        res.put("avgSewageCoveragePct", Math.round(avgSewage * 10.0) / 10.0);
        res.put("avgRoadPci", Math.round(avgPci * 10.0) / 10.0);
        return ResponseEntity.ok(res);
    }
}
