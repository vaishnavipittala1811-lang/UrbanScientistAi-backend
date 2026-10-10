package com.urbanscientist.telangana.service;

import com.urbanscientist.telangana.dto.ReportRequest;
import com.urbanscientist.telangana.dto.UrbanHealthScoreDto;
import com.urbanscientist.telangana.model.UrbanReport;
import com.urbanscientist.telangana.repository.UrbanReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    @Autowired
    private UrbanReportRepository reportRepository;

    @Autowired
    private UrbanIntelligenceService intelligenceService;

    public UrbanReport generateReport(ReportRequest request, String username) {
        UrbanHealthScoreDto healthScore = intelligenceService.calculateTelanganaHealthScore();

        String dist = request.getDistrict() != null && !request.getDistrict().isBlank()
                ? request.getDistrict() : "All Telangana";
        String city = request.getCity() != null && !request.getCity().isBlank()
                ? request.getCity() : "Statewide Urban Aggregate";

        String reportId = "TS-REP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        StringBuilder summary = new StringBuilder();
        if (request.getSummary() != null && !request.getSummary().isBlank()) {
            summary.append(request.getSummary());
        } else {
            summary.append("Comprehensive multi-sector urban risk audit for ").append(dist)
                    .append(" (").append(city).append("), Telangana, India. ")
                    .append("Current Telangana Urban Health Score is assessed at ")
                    .append(healthScore.getOverallScore()).append(" (")
                    .append(healthScore.getCategory()).append(" category). ")
                    .append("Identified key stressors across traffic mobility and groundwater depletion. ");
        }

        StringBuilder recommendations = new StringBuilder();
        if (request.getRecommendations() != null && !request.getRecommendations().isBlank()) {
            recommendations.append(request.getRecommendations());
        } else {
            recommendations.append("1. Immediate deployment of adaptive AI signal synchronizers on high-congestion corridors. ")
                    .append("2. Implement Mission Bhagiratha smart metering and pressure stabilization to mitigate water deficits. ")
                    .append("3. Augment peripheral Basti Dawakhana tele-clinics to absorb outpatient loads.");
        }

        UrbanReport report = new UrbanReport();
        report.setReportId(reportId);
        report.setTitle("UrbanScientist AI — Telangana Urban Intelligence Report");
        report.setSubtitle("AI-powered urban analysis and decision support for Telangana, India.");
        report.setGeographicScope("Telangana, India");
        report.setDistrict(dist);
        report.setCity(city);
        report.setHealthScore(healthScore.getOverallScore());
        report.setSummary(summary.toString());
        report.setRecommendations(recommendations.toString());
        report.setGeneratedAt(LocalDateTime.now());
        report.setGeneratedBy(username != null ? username : "UrbanScientist AI System");

        return reportRepository.save(report);
    }

    public List<UrbanReport> getAllReports() {
        return reportRepository.findAllByOrderByGeneratedAtDesc();
    }
}
