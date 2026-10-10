package com.urbanscientist.telangana.controller;

import com.urbanscientist.telangana.model.*;
import com.urbanscientist.telangana.repository.*;
import com.urbanscientist.telangana.service.UrbanIntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CityIntelligenceController {

    @Autowired
    private UrbanLocationRepository locationRepository;

    @Autowired
    private UrbanIntelligenceService intelligenceService;

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

    @GetMapping("/districts")
    public ResponseEntity<List<Map<String, Object>>> getDistricts() {
        List<UrbanLocation> locations = locationRepository.findAll();
        Map<String, List<UrbanLocation>> grouped = locations.stream().collect(Collectors.groupingBy(UrbanLocation::getDistrict));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<UrbanLocation>> entry : grouped.entrySet()) {
            String district = entry.getKey();
            List<UrbanLocation> locs = entry.getValue();

            UrbanLocation rep = locs.get(0);
            Set<String> cities = locs.stream().map(UrbanLocation::getCity).collect(Collectors.toSet());
            Set<String> zones = locs.stream().map(UrbanLocation::getZone).collect(Collectors.toSet());

            Map<String, Object> distMap = new HashMap<>();
            distMap.put("state", "Telangana");
            distMap.put("district", district);
            distMap.put("districtId", rep.getDistrictId());
            distMap.put("category", rep.getUrbanCategory());
            distMap.put("latitude", rep.getLatitude());
            distMap.put("longitude", rep.getLongitude());
            distMap.put("cityCount", cities.size());
            distMap.put("zoneCount", zones.size());
            distMap.put("cities", cities);

            result.add(distMap);
        }

        result.sort(Comparator.comparing(m -> (String) m.get("district")));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<Map<String, Object>>> getCities() {
        List<UrbanLocation> locations = locationRepository.findAll();
        Map<String, List<UrbanLocation>> grouped = locations.stream().collect(Collectors.groupingBy(UrbanLocation::getCity));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<UrbanLocation>> entry : grouped.entrySet()) {
            String city = entry.getKey();
            List<UrbanLocation> locs = entry.getValue();
            UrbanLocation rep = locs.get(0);

            Map<String, Object> cityMap = new HashMap<>();
            cityMap.put("state", "Telangana");
            cityMap.put("city", city);
            cityMap.put("cityId", rep.getCityId());
            cityMap.put("district", rep.getDistrict());
            cityMap.put("districtId", rep.getDistrictId());
            cityMap.put("latitude", rep.getLatitude());
            cityMap.put("longitude", rep.getLongitude());
            cityMap.put("zoneCount", locs.size());

            result.add(cityMap);
        }

        result.sort(Comparator.comparing(m -> (String) m.get("city")));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/locations/{locationId}")
    public ResponseEntity<?> getLocationDetails(@PathVariable String locationId) {
        UrbanLocation loc = locationRepository.findByLocationId(locationId).orElse(null);
        if (loc == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> detail = new HashMap<>();
        detail.put("location", loc);
        detail.put("traffic", trafficRepository.findByLocationId(locationId).orElse(null));
        detail.put("pollution", pollutionRepository.findByLocationId(locationId).orElse(null));
        detail.put("water", waterRepository.findByLocationId(locationId).orElse(null));
        detail.put("weather", weatherRepository.findByLocationId(locationId).orElse(null));
        detail.put("population", populationRepository.findByLocationId(locationId).orElse(null));
        detail.put("healthcare", healthcareRepository.findByLocationId(locationId).orElse(null));
        detail.put("infrastructure", infrastructureRepository.findByLocationId(locationId).orElse(null));

        return ResponseEntity.ok(detail);
    }

    @GetMapping("/locations/{locationId}/development-trends")
    public ResponseEntity<?> getDevelopmentTrends(@PathVariable String locationId) {
        UrbanLocation loc = locationRepository.findByLocationId(locationId).orElse(null);
        if (loc == null) {
            try {
                Long id = Long.parseLong(locationId);
                loc = locationRepository.findById(id).orElse(null);
            } catch (NumberFormatException ignored) {}
        }
        if (loc == null) {
            return ResponseEntity.notFound().build();
        }

        InfrastructureRecord infra = infrastructureRepository.findByLocationId(loc.getLocationId()).orElse(null);
        WeatherRecord weather = weatherRepository.findByLocationId(loc.getLocationId()).orElse(null);
        PollutionRecord pollution = pollutionRepository.findByLocationId(loc.getLocationId()).orElse(null);
        WaterRecord water = waterRepository.findByLocationId(loc.getLocationId()).orElse(null);

        double baseGreen = (infra != null && infra.getGreenCoverRatioPct() != null) ? infra.getGreenCoverRatioPct() : 31.5;
        double baseTemp = (weather != null && weather.getTemperatureC() != null) ? weather.getTemperatureC() : 34.8;
        int baseAqi = (pollution != null && pollution.getAqi() != null) ? pollution.getAqi() : 125;
        double baseWater = (water != null && water.getPipedWaterCoveragePct() != null) ? water.getPipedWaterCoveragePct() : 62.0;
        double baseBuiltUp = Math.min(88.0, Math.max(25.0, (100.0 - baseGreen) * 0.72));

        // Before Development (2019 to 2023)
        List<Integer> beforeYears = Arrays.asList(2019, 2020, 2021, 2022, 2023);
        List<Double> beforeGreen = Arrays.asList(
                round1(baseGreen + 6.2),
                round1(baseGreen + 4.8),
                round1(baseGreen + 3.1),
                round1(baseGreen + 1.2),
                round1(baseGreen)
        );
        List<Double> beforeTemp = Arrays.asList(
                round1(baseTemp - 1.8),
                round1(baseTemp - 1.2),
                round1(baseTemp - 0.7),
                round1(baseTemp - 0.3),
                round1(baseTemp)
        );
        List<Integer> beforeAqi = Arrays.asList(
                (int) Math.round(baseAqi * 0.82),
                (int) Math.round(baseAqi * 0.88),
                (int) Math.round(baseAqi * 0.93),
                (int) Math.round(baseAqi * 0.97),
                baseAqi
        );
        List<Double> beforeBuilt = Arrays.asList(
                round1(baseBuiltUp - 8.5),
                round1(baseBuiltUp - 6.0),
                round1(baseBuiltUp - 3.8),
                round1(baseBuiltUp - 1.5),
                round1(baseBuiltUp)
        );
        List<Double> beforeWater = Arrays.asList(
                round1(baseWater + 7.5),
                round1(baseWater + 5.0),
                round1(baseWater + 3.0),
                round1(baseWater + 1.2),
                round1(baseWater)
        );

        // After Development (2024 to 2028)
        List<Integer> afterYears = Arrays.asList(2024, 2025, 2026, 2027, 2028);
        List<Double> afterGreen = Arrays.asList(
                round1(baseGreen + 2.0),
                round1(baseGreen + 4.5),
                round1(baseGreen + 7.2),
                round1(baseGreen + 10.1),
                round1(baseGreen + 13.0)
        );
        List<Double> afterTemp = Arrays.asList(
                round1(baseTemp - 0.4),
                round1(baseTemp - 0.9),
                round1(baseTemp - 1.4),
                round1(baseTemp - 1.9),
                round1(baseTemp - 2.4)
        );
        List<Integer> afterAqi = Arrays.asList(
                (int) Math.round(baseAqi * 0.92),
                (int) Math.round(baseAqi * 0.84),
                (int) Math.round(baseAqi * 0.76),
                (int) Math.round(baseAqi * 0.69),
                (int) Math.round(baseAqi * 0.62)
        );
        List<Double> afterBuilt = Arrays.asList(
                round1(baseBuiltUp + 1.2),
                round1(baseBuiltUp + 2.2),
                round1(baseBuiltUp + 3.0),
                round1(baseBuiltUp + 3.6),
                round1(baseBuiltUp + 4.0)
        );
        List<Double> afterWater = Arrays.asList(
                round1(baseWater + 4.5),
                round1(baseWater + 9.0),
                round1(baseWater + 13.5),
                round1(baseWater + 17.5),
                round1(baseWater + 21.0)
        );

        Map<String, Object> beforeMap = new HashMap<>();
        beforeMap.put("years", beforeYears);
        beforeMap.put("greenCover", beforeGreen);
        beforeMap.put("surfaceTemperature", beforeTemp);
        beforeMap.put("airQuality", beforeAqi);
        beforeMap.put("builtUpArea", beforeBuilt);
        beforeMap.put("waterAvailability", beforeWater);

        Map<String, Object> afterMap = new HashMap<>();
        afterMap.put("years", afterYears);
        afterMap.put("greenCover", afterGreen);
        afterMap.put("surfaceTemperature", afterTemp);
        afterMap.put("airQuality", afterAqi);
        afterMap.put("builtUpArea", afterBuilt);
        afterMap.put("waterAvailability", afterWater);

        // Deltas: baseline (2023) vs projected (2028)
        double bG = beforeGreen.get(4);
        double aG = afterGreen.get(4);
        double bT = beforeTemp.get(4);
        double aT = afterTemp.get(4);
        int bA = beforeAqi.get(4);
        int aA = afterAqi.get(4);
        double bB = beforeBuilt.get(4);
        double aB = afterBuilt.get(4);
        double bW = beforeWater.get(4);
        double aW = afterWater.get(4);

        Map<String, Object> deltas = new HashMap<>();
        deltas.put("greenCover", Map.of(
                "before", bG,
                "after", aG,
                "changeAbs", round1(aG - bG),
                "changePct", round1(((aG - bG) / bG) * 100.0)
        ));
        deltas.put("surfaceTemperature", Map.of(
                "before", bT,
                "after", aT,
                "changeAbs", round1(aT - bT),
                "changePct", round1(((aT - bT) / bT) * 100.0)
        ));
        deltas.put("airQuality", Map.of(
                "before", bA,
                "after", aA,
                "changeAbs", aA - bA,
                "changePct", round1(((double)(aA - bA) / bA) * 100.0)
        ));
        deltas.put("builtUpArea", Map.of(
                "before", bB,
                "after", aB,
                "changeAbs", round1(aB - bB),
                "changePct", round1(((aB - bB) / bB) * 100.0)
        ));
        deltas.put("waterAvailability", Map.of(
                "before", bW,
                "after", aW,
                "changeAbs", round1(aW - bW),
                "changePct", round1(((aW - bW) / bW) * 100.0)
        ));

        Map<String, Object> res = new HashMap<>();
        res.put("locationId", loc.getLocationId());
        res.put("locationName", loc.getLocationName());
        res.put("district", loc.getDistrict());
        res.put("city", loc.getCity());
        res.put("zone", loc.getZone());
        res.put("beforeDevelopment", beforeMap);
        res.put("afterDevelopment", afterMap);
        res.put("deltas", deltas);

        return ResponseEntity.ok(res);
    }

    private double round1(double val) {
        return Math.round(val * 10.0) / 10.0;
    }
}
