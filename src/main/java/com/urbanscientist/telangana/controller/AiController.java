package com.urbanscientist.telangana.controller;

import com.urbanscientist.telangana.dto.BudgetOptimizationRequest;
import com.urbanscientist.telangana.dto.BudgetOptimizationResult;
import com.urbanscientist.telangana.dto.SimulationRequest;
import com.urbanscientist.telangana.dto.SimulationResult;
import com.urbanscientist.telangana.model.Intervention;
import com.urbanscientist.telangana.repository.InterventionRepository;
import com.urbanscientist.telangana.service.AiInsightsService;
import com.urbanscientist.telangana.service.BudgetOptimizerService;
import com.urbanscientist.telangana.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private AiInsightsService aiInsightsService;

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private BudgetOptimizerService budgetOptimizerService;

    @Autowired
    private InterventionRepository interventionRepository;

    @GetMapping("/insights")
    public ResponseEntity<List<Map<String, Object>>> getAiInsights() {
        return ResponseEntity.ok(aiInsightsService.generateTelanganaAiInsights());
    }

    @GetMapping("/interventions")
    public ResponseEntity<List<Intervention>> getInterventions() {
        return ResponseEntity.ok(interventionRepository.findAll());
    }

    @PostMapping("/simulate")
    public ResponseEntity<SimulationResult> runSimulation(@RequestBody SimulationRequest request) {
        return ResponseEntity.ok(simulationService.runSimulation(request));
    }

    @PostMapping("/optimize-budget")
    public ResponseEntity<BudgetOptimizationResult> optimizeBudget(@RequestBody BudgetOptimizationRequest request) {
        return ResponseEntity.ok(budgetOptimizerService.optimizeBudget(request));
    }
}
