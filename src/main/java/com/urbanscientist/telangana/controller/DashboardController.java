package com.urbanscientist.telangana.controller;

import com.urbanscientist.telangana.dto.UrbanHealthScoreDto;
import com.urbanscientist.telangana.model.*;
import com.urbanscientist.telangana.repository.*;
import com.urbanscientist.telangana.service.UrbanIntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private UrbanIntelligenceService intelligenceService;

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
    private PopulationRepository populationRepository;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        UrbanHealthScoreDto healthScore = intelligenceService.calculateTelanganaHealthScore();
        List<TrafficRecord> traffic = trafficRepository.findAll();
        List<PollutionRecord> pollution = pollutionRepository.findAll();
        List<WaterRecord> water = waterRepository.findAll();
        List<HealthcareRecord> hc = healthcareRepository.findAll();
        List<PopulationRecord> pop = populationRepository.findAll();

        long totalPop = pop.stream().mapToLong(p -> p.getCurrentPopulation() != null ? p.getCurrentPopulation() : 0L).sum();
        double avgAqi = pollution.stream().mapToInt(p -> p.getAqi() != null ? p.getAqi() : 0).average().orElse(0.0);
        double avgCongestion = traffic.stream().mapToDouble(t -> t.getCongestionLevelPct() != null ? t.getCongestionLevelPct() : 0.0).average().orElse(0.0);
        double avgWaterDeficit = water.stream().mapToDouble(w -> w.getSupplyDeficitPct() != null ? w.getSupplyDeficitPct() : 0.0).average().orElse(0.0);
        long activeHotspots = traffic.stream().filter(t -> t.getHotspotFlag() != null && t.getHotspotFlag() == 1).count();

        Map<String, Object> response = new HashMap<>();
        response.put("productTitle", "UrbanScientist AI — Telangana");
        response.put("subtitle", "Autonomous Urban Intelligence & Decision Support Platform for Telangana");
        response.put("geographicScope", "Telangana, India");
        response.put("locationCount", locationRepository.count());
        response.put("totalMonitoredPopulation", totalPop);
        response.put("healthScore", healthScore.getOverallScore());
        response.put("healthCategory", healthScore.getCategory());
        response.put("activeHotspots", activeHotspots);
        response.put("averageAqi", Math.round(avgAqi));
        response.put("averageCongestionPct", Math.round(avgCongestion * 10.0) / 10.0);
        response.put("averageWaterDeficitPct", Math.round(avgWaterDeficit * 10.0) / 10.0);
        response.put("healthBreakdown", healthScore);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<Map<String, Object>>> getComprehensiveLocations() {
        return ResponseEntity.ok(intelligenceService.getComprehensiveLocations());
    }

    @GetMapping("/health-score")
    public ResponseEntity<UrbanHealthScoreDto> getHealthScore() {
        return ResponseEntity.ok(intelligenceService.calculateTelanganaHealthScore());
    }
}
