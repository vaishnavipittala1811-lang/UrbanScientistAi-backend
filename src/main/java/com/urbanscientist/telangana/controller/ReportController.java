package com.urbanscientist.telangana.controller;

import com.urbanscientist.telangana.dto.ReportRequest;
import com.urbanscientist.telangana.model.UrbanReport;
import com.urbanscientist.telangana.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<UrbanReport> generateReport(@RequestBody ReportRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "Urban Planner / Analyst";
        return ResponseEntity.ok(reportService.generateReport(request, username));
    }

    @GetMapping
    public ResponseEntity<List<UrbanReport>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }
}
