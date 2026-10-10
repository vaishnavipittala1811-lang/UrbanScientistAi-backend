package com.urbanscientist.telangana.controller;

import com.urbanscientist.telangana.service.DatasetGisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/data-sources")
@CrossOrigin(origins = "*")
public class DataSourceController {

    @Autowired
    private DatasetGisService datasetGisService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getDataSources() {
        List<Map<String, Object>> sources = Arrays.asList(
                Map.of(
                        "dataset", "locations.csv",
                        "module", "Spatial / GIS",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 71,
                        "status", "ACTIVE",
                        "dataType", "TELANGANA MUNICIPAL REGISTRY",
                        "source", "UrbanScientist AI Geo-Engineering Lab (Calibrated with Telangana Open Spatial Boundaries)",
                        "createdDate", "2026-10-05",
                        "description", "Hierarchical administrative mapping across all 33 Telangana districts, municipalities, and zones with GIS coordinates."
                ),
                Map.of(
                        "dataset", "traffic.csv",
                        "module", "Traffic Intelligence",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 71,
                        "status", "ACTIVE",
                        "dataType", "MOBILITY SENSOR TELEMETRY",
                        "source", "Urban Mobility Telemetry & Traffic Flow Modeling",
                        "createdDate", "2026-10-05",
                        "description", "Arterial speeds, peak delays, vehicular densities, transit coverage, and congestion indices across urban nodes."
                ),
                Map.of(
                        "dataset", "pollution.csv",
                        "module", "Pollution Intelligence",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 71,
                        "status", "ACTIVE",
                        "dataType", "ATMOSPHERIC DISPERSION GRID",
                        "source", "Atmospheric Dispersion & Continuous Ambient Air Quality Monitoring",
                        "createdDate", "2026-10-05",
                        "description", "Particulate matters (PM2.5, PM10), gaseous pollutants (NO2, SO2, CO), industrial emission index, and computed AQI."
                ),
                Map.of(
                        "dataset", "water.csv",
                        "module", "Water Intelligence",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 71,
                        "status", "ACTIVE",
                        "dataType", "HYDROLOGICAL GRID TELEMETRY",
                        "source", "Hydrological Stress, Mission Bhagiratha Distribution & Groundwater Monitoring",
                        "createdDate", "2026-10-05",
                        "description", "Groundwater depth, municipal supply deficit, water quality index (WQI), piped coverage, and flood vulnerability indices."
                ),
                Map.of(
                        "dataset", "weather.csv",
                        "module", "Weather Intelligence",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 71,
                        "status", "ACTIVE",
                        "dataType", "MICROCLIMATE THERMAL RADAR",
                        "source", "Urban Microclimate & Thermal Imaging Sensor Network",
                        "createdDate", "2026-10-05",
                        "description", "Ambient temperature, perceived heat index, urban heat island (UHI) intensity, relative humidity, and precipitation."
                ),
                Map.of(
                        "dataset", "population.csv",
                        "module", "Population Intelligence",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 71,
                        "status", "ACTIVE",
                        "dataType", "DEMOGRAPHIC PROJECTION MODEL",
                        "source", "Demographic Projection Engine (Polynomial & Growth Cohort Models)",
                        "createdDate", "2026-10-05",
                        "description", "Zone populations, densities per sq km, annual growth rates, and 5/10/15-year forward urbanization demand projections."
                ),
                Map.of(
                        "dataset", "healthcare.csv",
                        "module", "Healthcare Intelligence",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 71,
                        "status", "ACTIVE",
                        "dataType", "EPIDEMIOLOGICAL SURGE MODEL",
                        "source", "Healthcare Capacity, PHC Registry & Emergency Medical Telemetry",
                        "createdDate", "2026-10-05",
                        "description", "Hospital beds per 1k, primary health center distribution, ICU occupancy rates, and ambulance response telemetry."
                ),
                Map.of(
                        "dataset", "infrastructure.csv",
                        "module", "Infrastructure Intelligence",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 71,
                        "status", "ACTIVE",
                        "dataType", "MUNICIPAL ASSET TELEMETRY",
                        "source", "Municipal Asset Telemetry, Green Cover (NDVI) & Pavement Condition Model",
                        "createdDate", "2026-10-05",
                        "description", "Power grid reliability, sewage network penetration, solid waste generation, green cover ratio, and pavement condition index (PCI)."
                ),
                Map.of(
                        "dataset", "interventions.csv",
                        "module", "What-If Simulator & Intervention Planner",
                        "geographicCoverage", "TELANGANA, INDIA",
                        "records", 8,
                        "status", "ACTIVE",
                        "dataType", "CIVIC INTERVENTION CATALOG",
                        "source", "Urban Decision Support System Intervention Catalog",
                        "createdDate", "2026-10-05",
                        "description", "Actionable multi-sector civil interventions with quantified CAPEX (₹ Crores), timeline, risk reduction, and expected ROI."
                )
        );

        return ResponseEntity.ok(sources);
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Uploaded file is empty."));
        }
        try {
            String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "uploaded_dataset.csv";
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            Map<String, Object> analysis = datasetGisService.analyzeCsv(fileName, content, null);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to parse CSV file: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/analyze-content", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> analyzeContent(@RequestBody Map<String, Object> payload) {
        String fileName = payload.getOrDefault("fileName", "dataset.csv").toString();
        String content = payload.getOrDefault("content", "").toString();
        @SuppressWarnings("unchecked")
        Map<String, String> overrides = (Map<String, String>) payload.get("fieldOverrides");

        if (content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "CSV content cannot be empty."));
        }

        try {
            Map<String, Object> analysis = datasetGisService.analyzeCsv(fileName, content, overrides);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Analysis failed: " + e.getMessage()));
        }
    }

    @GetMapping("/catalog-analysis/{fileName}")
    public ResponseEntity<?> analyzeCatalog(@PathVariable String fileName) {
        try {
            Map<String, Object> analysis = datasetGisService.loadCatalogDataset(fileName);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadCatalog(@PathVariable String fileName) {
        try {
            Resource resource = new ClassPathResource("data/" + fileName);
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
