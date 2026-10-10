package com.urbanscientist.telangana.service;

import com.urbanscientist.telangana.model.UrbanLocation;
import com.urbanscientist.telangana.repository.UrbanLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class DatasetGisService {

    @Autowired
    private UrbanLocationRepository locationRepository;

    // Static registry of major Indian cities with exact geographic coordinates for national datasets
    public static class IndianCityGeo {
        public final String name;
        public final String state;
        public final double latitude;
        public final double longitude;

        public IndianCityGeo(String name, String state, double latitude, double longitude) {
            this.name = name;
            this.state = state;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private static final Map<String, IndianCityGeo> INDIAN_CITIES = new HashMap<>();

    static {
        registerCity("Ahmedabad", "Gujarat", 23.0225, 72.5714);
        registerCity("Aizawl", "Mizoram", 23.7271, 92.7176);
        registerCity("Amaravati", "Andhra Pradesh", 16.5131, 80.5165);
        registerCity("Amritsar", "Punjab", 31.6340, 74.8723);
        registerCity("Bengaluru", "Karnataka", 12.9716, 77.5946);
        registerCity("Bangalore", "Karnataka", 12.9716, 77.5946);
        registerCity("Bhopal", "Madhya Pradesh", 23.2599, 77.4126);
        registerCity("Brajrajnagar", "Odisha", 21.8248, 83.9160);
        registerCity("Chandigarh", "Chandigarh", 30.7333, 76.7794);
        registerCity("Chennai", "Tamil Nadu", 13.0827, 80.2707);
        registerCity("Coimbatore", "Tamil Nadu", 11.0168, 76.9558);
        registerCity("Delhi", "Delhi", 28.6139, 77.2090);
        registerCity("New Delhi", "Delhi", 28.6139, 77.2090);
        registerCity("Ernakulam", "Kerala", 9.9816, 76.2999);
        registerCity("Gurugram", "Haryana", 28.4595, 77.0266);
        registerCity("Gurgaon", "Haryana", 28.4595, 77.0266);
        registerCity("Guwahati", "Assam", 26.1445, 91.7362);
        registerCity("Hyderabad", "Telangana", 17.3850, 78.4867);
        registerCity("Jaipur", "Rajasthan", 26.9124, 75.7873);
        registerCity("Jorapokhar", "Jharkhand", 23.7058, 86.4132);
        registerCity("Kochi", "Kerala", 9.9312, 76.2673);
        registerCity("Cochin", "Kerala", 9.9312, 76.2673);
        registerCity("Kolkata", "West Bengal", 22.5726, 88.3639);
        registerCity("Calcutta", "West Bengal", 22.5726, 88.3639);
        registerCity("Lucknow", "Uttar Pradesh", 26.8467, 80.9462);
        registerCity("Mumbai", "Maharashtra", 19.0760, 72.8777);
        registerCity("Bombay", "Maharashtra", 19.0760, 72.8777);
        registerCity("Patna", "Bihar", 25.5941, 85.1376);
        registerCity("Shillong", "Meghalaya", 25.5788, 91.8933);
        registerCity("Talcher", "Odisha", 20.9509, 85.2166);
        registerCity("Thiruvananthapuram", "Kerala", 8.5241, 76.9366);
        registerCity("Trivandrum", "Kerala", 8.5241, 76.9366);
        registerCity("Visakhapatnam", "Andhra Pradesh", 17.6868, 83.2185);
        registerCity("Vizag", "Andhra Pradesh", 17.6868, 83.2185);
        registerCity("Pune", "Maharashtra", 18.5204, 73.8567);
        registerCity("Surat", "Gujarat", 21.1702, 72.8311);
        registerCity("Indore", "Madhya Pradesh", 22.7196, 75.8577);
        registerCity("Nagpur", "Maharashtra", 21.1458, 79.0882);
        registerCity("Varanasi", "Uttar Pradesh", 25.3176, 82.9739);
        registerCity("Noida", "Uttar Pradesh", 28.5355, 77.3910);
        registerCity("Ghaziabad", "Uttar Pradesh", 28.6692, 77.4538);
        registerCity("Kanpur", "Uttar Pradesh", 26.4499, 80.3319);
        registerCity("Agra", "Uttar Pradesh", 27.1767, 78.0081);
        registerCity("Vadodara", "Gujarat", 22.3072, 73.1812);
        registerCity("Warangal", "Telangana", 17.9689, 79.5941);
        registerCity("Karimnagar", "Telangana", 18.4386, 79.1288);
        registerCity("Nizamabad", "Telangana", 18.6725, 78.0941);
        registerCity("Khammam", "Telangana", 17.2473, 80.1514);
        registerCity("Ramagundam", "Telangana", 18.7557, 79.4738);
        registerCity("Mahbubnagar", "Telangana", 16.7488, 77.9856);
        registerCity("Nalgonda", "Telangana", 17.0575, 79.2684);
        registerCity("Adilabad", "Telangana", 19.6641, 78.5320);
        registerCity("Siddipet", "Telangana", 18.1018, 78.8520);
    }

    private static void registerCity(String name, String state, double lat, double lng) {
        INDIAN_CITIES.put(normalizeKey(name), new IndianCityGeo(name, state, lat, lng));
    }

    private static String normalizeKey(String str) {
        if (str == null) return "";
        return str.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private static final Set<String> KNOWN_CATEGORICAL_NAMES = Set.of(
            "aqi_bucket", "aqibucket", "bucket", "category", "status", "city", "state", "district",
            "zone", "location", "location_name", "locationname", "name", "type", "description",
            "date", "datetime", "timestamp", "year", "month", "day", "time"
    );

    private static final Set<String> KNOWN_AIR_QUALITY_METRICS = Set.of(
            "pm2.5", "pm25", "pm10", "no", "no2", "nox", "nh3", "co", "so2", "o3",
            "benzene", "toluene", "xylene", "aqi"
    );

    public Map<String, Object> analyzeCsv(String fileName, String csvContent, Map<String, String> manualFieldOverrides) {
        List<List<String>> rawRows = parseCsv(csvContent);
        if (rawRows.isEmpty()) {
            throw new IllegalArgumentException("Uploaded CSV is empty or cannot be parsed.");
        }

        List<String> rawHeaders = rawRows.get(0);
        List<String> headers = new ArrayList<>();
        for (String h : rawHeaders) {
            String trimmed = h.trim().replace("\uFEFF", ""); // remove UTF-8 BOM if present
            headers.add(trimmed);
        }

        List<Map<String, Object>> records = new ArrayList<>();
        for (int i = 1; i < rawRows.size(); i++) {
            List<String> row = rawRows.get(i);
            // Check if entire row is empty or only empty strings
            boolean allBlank = true;
            for (String val : row) {
                if (val != null && !val.trim().isEmpty()) {
                    allBlank = false;
                    break;
                }
            }
            if (row.isEmpty() || (row.size() == 1 && row.get(0).trim().isEmpty())) {
                continue;
            }

            Map<String, Object> record = new LinkedHashMap<>();
            for (int col = 0; col < headers.size(); col++) {
                String header = headers.get(col);
                String val = col < row.size() ? row.get(col).trim() : "";
                record.put(header, val);
            }
            record.put("_rowIndex", i);
            records.add(record);
        }

        // Automatic Column Detection
        Map<String, String> detectedGeo = detectGeographicFields(headers);
        if (manualFieldOverrides != null) {
            for (Map.Entry<String, String> entry : manualFieldOverrides.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().trim().isEmpty() && !"none".equalsIgnoreCase(entry.getValue())) {
                    detectedGeo.put(entry.getKey(), entry.getValue().trim());
                }
            }
        }

        // Detect Temporal & Categorical Columns
        List<String> temporalColumns = new ArrayList<>();
        List<String> categoricalColumns = new ArrayList<>();
        String dateField = null;

        for (String header : headers) {
            String norm = normalizeKey(header);
            if (norm.equals("date") || norm.equals("datetime") || norm.equals("timestamp") || norm.contains("date")) {
                temporalColumns.add(header);
                if (dateField == null) dateField = header;
            } else if (norm.equals("aqibucket") || norm.equals("aqi_bucket") || norm.contains("bucket") || norm.equals("status") || norm.equals("category")) {
                categoricalColumns.add(header);
            }
        }

        // Detect Numeric & Text Columns
        List<String> numericColumns = new ArrayList<>();
        List<String> textColumns = new ArrayList<>();
        Map<String, Map<String, Object>> numericStats = new LinkedHashMap<>();
        Map<String, Map<String, Object>> missingValueSummary = new LinkedHashMap<>();

        for (String header : headers) {
            // Compute missing value stats for every column
            missingValueSummary.put(header, computeMissingStats(records, header));

            boolean isNumeric = isColumnNumeric(records, header);
            if (isNumeric) {
                numericColumns.add(header);
                numericStats.put(header, computeColumnStats(records, header));
            } else {
                textColumns.add(header);
            }
        }

        // Reference Locations from Telangana DB
        List<UrbanLocation> refLocations = locationRepository.findAll();
        Map<String, UrbanLocation> refByLocId = new HashMap<>();
        Map<String, UrbanLocation> refByDistrict = new HashMap<>();
        Map<String, UrbanLocation> refByCity = new HashMap<>();
        Map<String, UrbanLocation> refByZone = new HashMap<>();

        for (UrbanLocation loc : refLocations) {
            if (loc.getLocationId() != null) refByLocId.put(normalizeKey(loc.getLocationId()), loc);
            if (loc.getDistrict() != null) refByDistrict.putIfAbsent(normalizeKey(loc.getDistrict()), loc);
            if (loc.getCity() != null) refByCity.putIfAbsent(normalizeKey(loc.getCity()), loc);
            if (loc.getZone() != null) refByZone.putIfAbsent(normalizeKey(loc.getZone()), loc);
        }

        int mappedCount = 0;
        int unmappedCount = 0;
        List<Map<String, Object>> mappedRecords = new ArrayList<>();
        List<Map<String, Object>> unmappedRecords = new ArrayList<>();

        Double minLat = null, maxLat = null, minLng = null, maxLng = null;

        String latField = detectedGeo.get("latitude");
        String lngField = detectedGeo.get("longitude");
        String distField = detectedGeo.get("district");
        String cityField = detectedGeo.get("city");
        String zoneField = detectedGeo.get("zone");
        String locIdField = detectedGeo.get("locationId");

        for (Map<String, Object> rec : records) {
            Double lat = parseDouble(rec.get(latField));
            Double lng = parseDouble(rec.get(lngField));
            String mappingType = "NONE";
            String matchedReference = null;

            if (lat != null && lng != null && isValidCoordinate(lat, lng)) {
                mappingType = "POINT_COORDINATES";
            } else {
                String locIdVal = locIdField != null && rec.get(locIdField) != null ? normalizeKey(rec.get(locIdField).toString()) : null;
                String zoneVal = zoneField != null && rec.get(zoneField) != null ? normalizeKey(rec.get(zoneField).toString()) : null;
                String distVal = distField != null && rec.get(distField) != null ? normalizeKey(rec.get(distField).toString()) : null;
                String cityVal = cityField != null && rec.get(cityField) != null ? normalizeKey(rec.get(cityField).toString()) : null;

                // 1. Try matching Telangana Database
                UrbanLocation matchedRef = null;
                if (locIdVal != null && !locIdVal.isEmpty() && refByLocId.containsKey(locIdVal)) {
                    matchedRef = refByLocId.get(locIdVal);
                    mappingType = "TELANGANA_LOCATION_ID";
                } else if (zoneVal != null && !zoneVal.isEmpty() && refByZone.containsKey(zoneVal)) {
                    matchedRef = refByZone.get(zoneVal);
                    mappingType = "TELANGANA_ZONE_MATCH";
                } else if (cityVal != null && !cityVal.isEmpty() && refByCity.containsKey(cityVal)) {
                    matchedRef = refByCity.get(cityVal);
                    mappingType = "TELANGANA_CITY_MATCH";
                } else if (distVal != null && !distVal.isEmpty() && refByDistrict.containsKey(distVal)) {
                    matchedRef = refByDistrict.get(distVal);
                    mappingType = "TELANGANA_DISTRICT_MATCH";
                }

                if (matchedRef != null) {
                    lat = matchedRef.getLatitude();
                    lng = matchedRef.getLongitude();
                    matchedReference = matchedRef.getLocationName() + " (" + matchedRef.getDistrict() + ", Telangana)";
                } else {
                    // 2. Try matching National Indian City Reference Registry
                    if (cityVal != null && !cityVal.isEmpty() && INDIAN_CITIES.containsKey(cityVal)) {
                        IndianCityGeo natCity = INDIAN_CITIES.get(cityVal);
                        lat = natCity.latitude;
                        lng = natCity.longitude;
                        mappingType = "CITY_REGISTRY_MATCH";
                        matchedReference = natCity.name + " (" + natCity.state + ", India)";
                    } else if (distVal != null && !distVal.isEmpty() && INDIAN_CITIES.containsKey(distVal)) {
                        IndianCityGeo natCity = INDIAN_CITIES.get(distVal);
                        lat = natCity.latitude;
                        lng = natCity.longitude;
                        mappingType = "CITY_REGISTRY_MATCH";
                        matchedReference = natCity.name + " (" + natCity.state + ", India)";
                    }
                }
            }

            if (lat != null && lng != null && isValidCoordinate(lat, lng)) {
                mappedCount++;
                rec.put("_lat", lat);
                rec.put("_lng", lng);
                rec.put("_mappingType", mappingType);
                if (matchedReference != null) rec.put("_matchedReference", matchedReference);
                // Keep representative sample of mapped records for frontend performance
                if (mappedRecords.size() < 1000) {
                    mappedRecords.add(rec);
                }

                if (minLat == null || lat < minLat) minLat = lat;
                if (maxLat == null || lat > maxLat) maxLat = lat;
                if (minLng == null || lng < minLng) minLng = lng;
                if (maxLng == null || lng > maxLng) maxLng = lng;
            } else {
                unmappedCount++;
                rec.put("_mappingType", "UNMAPPED");
                rec.put("_unmappedReason", "Missing coordinates and location name not found in GIS reference database.");
                if (unmappedRecords.size() < 200) {
                    unmappedRecords.add(rec);
                }
            }
        }

        // Air Quality Analysis & Aggregation
        Map<String, Object> airQualityAnalysis = buildAirQualityAnalysis(records, cityField, dateField);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("fileName", fileName);
        result.put("totalRecords", records.size());
        result.put("recordsAnalyzed", records.size());
        result.put("mappedCount", mappedCount);
        result.put("unmappedCount", unmappedCount);
        result.put("unmatchedLocationsCount", unmappedCount);
        result.put("hasGeographicData", mappedCount > 0);
        result.put("detectedGeoFields", detectedGeo);
        result.put("allColumns", headers);
        result.put("numericColumns", numericColumns);
        result.put("textColumns", textColumns);
        result.put("temporalColumns", temporalColumns);
        result.put("categoricalColumns", categoricalColumns);
        result.put("numericStats", numericStats);
        result.put("missingValueSummary", missingValueSummary);
        result.put("airQualityAnalysis", airQualityAnalysis);
        result.put("sampleRecords", records.subList(0, Math.min(15, records.size())));
        result.put("mappedRecords", mappedRecords);
        result.put("unmappedRecords", unmappedRecords);

        Map<String, Object> bounds = new HashMap<>();
        bounds.put("minLat", minLat != null ? minLat : 8.0);
        bounds.put("maxLat", maxLat != null ? maxLat : 33.0);
        bounds.put("minLng", minLng != null ? minLng : 68.0);
        bounds.put("maxLng", maxLng != null ? maxLng : 97.0);
        bounds.put("centerLat", minLat != null && maxLat != null ? (minLat + maxLat) / 2.0 : 20.5937);
        bounds.put("centerLng", minLng != null && maxLng != null ? (minLng + maxLng) / 2.0 : 78.9629);
        result.put("bounds", bounds);

        return result;
    }

    private Map<String, Object> buildAirQualityAnalysis(List<Map<String, Object>> records, String cityField, String dateField) {
        Map<String, Object> aq = new LinkedHashMap<>();
        if (records.isEmpty()) {
            aq.put("hasAirQualityData", false);
            return aq;
        }

        // Check if any air quality columns exist in the record
        Map<String, Object> firstRow = records.get(0);
        boolean hasPollutant = false;
        for (String k : firstRow.keySet()) {
            String norm = normalizeKey(k);
            if (KNOWN_AIR_QUALITY_METRICS.contains(norm) || norm.contains("aqi") || norm.contains("pm25") || norm.contains("pm10")) {
                hasPollutant = true;
                break;
            }
        }

        aq.put("hasAirQualityData", hasPollutant);
        if (!hasPollutant) {
            return aq;
        }

        // Find relevant column names in this dataset
        String aqiCol = findColumn(firstRow, "aqi");
        String pm25Col = findColumn(firstRow, "pm25", "pm2.5");
        String pm10Col = findColumn(firstRow, "pm10");
        String no2Col = findColumn(firstRow, "no2");
        String so2Col = findColumn(firstRow, "so2");
        String coCol = findColumn(firstRow, "co");
        String bucketCol = findColumn(firstRow, "aqibucket", "aqi_bucket", "bucket");

        // Aggregation by City
        Map<String, CityPollutionAggregator> cityMap = new LinkedHashMap<>();
        Map<String, Integer> bucketCounts = new LinkedHashMap<>();
        bucketCounts.put("Good", 0);
        bucketCounts.put("Satisfactory", 0);
        bucketCounts.put("Moderate", 0);
        bucketCounts.put("Poor", 0);
        bucketCounts.put("Very Poor", 0);
        bucketCounts.put("Severe", 0);

        String minDate = null;
        String maxDate = null;

        double totalAqiSum = 0;
        int totalAqiCount = 0;
        double totalPm25Sum = 0;
        int totalPm25Count = 0;
        double totalPm10Sum = 0;
        int totalPm10Count = 0;
        double totalNo2Sum = 0;
        int totalNo2Count = 0;
        double totalSo2Sum = 0;
        int totalSo2Count = 0;
        double totalCoSum = 0;
        int totalCoCount = 0;

        for (Map<String, Object> rec : records) {
            String cityName = cityField != null && rec.get(cityField) != null && !isMissingValue(rec.get(cityField).toString())
                    ? rec.get(cityField).toString().trim()
                    : "National / Unspecified";

            CityPollutionAggregator agg = cityMap.computeIfAbsent(cityName, k -> new CityPollutionAggregator(cityName));
            agg.recordCount++;

            // AQI
            Double aqi = aqiCol != null ? parseDouble(rec.get(aqiCol)) : null;
            if (aqi != null) {
                agg.aqiSum += aqi;
                agg.aqiCount++;
                totalAqiSum += aqi;
                totalAqiCount++;
            }

            // PM2.5
            Double pm25 = pm25Col != null ? parseDouble(rec.get(pm25Col)) : null;
            if (pm25 != null) {
                agg.pm25Sum += pm25;
                agg.pm25Count++;
                totalPm25Sum += pm25;
                totalPm25Count++;
            }

            // PM10
            Double pm10 = pm10Col != null ? parseDouble(rec.get(pm10Col)) : null;
            if (pm10 != null) {
                agg.pm10Sum += pm10;
                agg.pm10Count++;
                totalPm10Sum += pm10;
                totalPm10Count++;
            }

            // NO2
            Double no2 = no2Col != null ? parseDouble(rec.get(no2Col)) : null;
            if (no2 != null) {
                agg.no2Sum += no2;
                agg.no2Count++;
                totalNo2Sum += no2;
                totalNo2Count++;
            }

            // SO2
            Double so2 = so2Col != null ? parseDouble(rec.get(so2Col)) : null;
            if (so2 != null) {
                agg.so2Sum += so2;
                agg.so2Count++;
                totalSo2Sum += so2;
                totalSo2Count++;
            }

            // CO
            Double co = coCol != null ? parseDouble(rec.get(coCol)) : null;
            if (co != null) {
                agg.coSum += co;
                agg.coCount++;
                totalCoSum += co;
                totalCoCount++;
            }

            // AQI Bucket
            String bucketVal = bucketCol != null && rec.get(bucketCol) != null ? rec.get(bucketCol).toString().trim() : null;
            if (bucketVal != null && !isMissingValue(bucketVal)) {
                agg.bucketCounts.put(bucketVal, agg.bucketCounts.getOrDefault(bucketVal, 0) + 1);
                bucketCounts.put(bucketVal, bucketCounts.getOrDefault(bucketVal, 0) + 1);
            } else if (aqi != null) {
                String derived = deriveAqiBucket(aqi);
                agg.bucketCounts.put(derived, agg.bucketCounts.getOrDefault(derived, 0) + 1);
                bucketCounts.put(derived, bucketCounts.getOrDefault(derived, 0) + 1);
            }

            // Date tracking
            if (dateField != null && rec.get(dateField) != null) {
                String dStr = rec.get(dateField).toString().trim();
                if (!isMissingValue(dStr)) {
                    if (minDate == null || dStr.compareTo(minDate) < 0) minDate = dStr;
                    if (maxDate == null || dStr.compareTo(maxDate) > 0) maxDate = dStr;
                }
            }
        }

        // Build City Summaries
        List<Map<String, Object>> citySummaries = new ArrayList<>();
        for (CityPollutionAggregator agg : cityMap.values()) {
            Map<String, Object> cMap = new LinkedHashMap<>();
            cMap.put("city", agg.cityName);
            cMap.put("recordCount", agg.recordCount);
            cMap.put("avgAqi", agg.aqiCount > 0 ? Math.round((agg.aqiSum / agg.aqiCount) * 10.0) / 10.0 : null);
            cMap.put("avgPm25", agg.pm25Count > 0 ? Math.round((agg.pm25Sum / agg.pm25Count) * 10.0) / 10.0 : null);
            cMap.put("avgPm10", agg.pm10Count > 0 ? Math.round((agg.pm10Sum / agg.pm10Count) * 10.0) / 10.0 : null);
            cMap.put("avgNo2", agg.no2Count > 0 ? Math.round((agg.no2Sum / agg.no2Count) * 10.0) / 10.0 : null);
            cMap.put("avgSo2", agg.so2Count > 0 ? Math.round((agg.so2Sum / agg.so2Count) * 10.0) / 10.0 : null);
            cMap.put("avgCo", agg.coCount > 0 ? Math.round((agg.coSum / agg.coCount) * 100.0) / 100.0 : null);

            // Dominant bucket
            String dominantBucket = null;
            int maxCount = -1;
            for (Map.Entry<String, Integer> bEntry : agg.bucketCounts.entrySet()) {
                if (bEntry.getValue() > maxCount) {
                    maxCount = bEntry.getValue();
                    dominantBucket = bEntry.getKey();
                }
            }
            if (dominantBucket == null && agg.aqiCount > 0) {
                dominantBucket = deriveAqiBucket(agg.aqiSum / agg.aqiCount);
            }
            cMap.put("dominantBucket", dominantBucket);

            // Geo info
            String normCity = normalizeKey(agg.cityName);
            if (INDIAN_CITIES.containsKey(normCity)) {
                IndianCityGeo geo = INDIAN_CITIES.get(normCity);
                cMap.put("isMapped", true);
                cMap.put("lat", geo.latitude);
                cMap.put("lng", geo.longitude);
                cMap.put("state", geo.state);
            } else {
                cMap.put("isMapped", false);
            }

            citySummaries.add(cMap);
        }

        // Sort city summaries by recordCount descending
        citySummaries.sort((a, b) -> Integer.compare((Integer) b.get("recordCount"), (Integer) a.get("recordCount")));

        // Overall summary
        Map<String, Object> overall = new LinkedHashMap<>();
        overall.put("avgAqi", totalAqiCount > 0 ? Math.round((totalAqiSum / totalAqiCount) * 10.0) / 10.0 : null);
        overall.put("avgPm25", totalPm25Count > 0 ? Math.round((totalPm25Sum / totalPm25Count) * 10.0) / 10.0 : null);
        overall.put("avgPm10", totalPm10Count > 0 ? Math.round((totalPm10Sum / totalPm10Count) * 10.0) / 10.0 : null);
        overall.put("avgNo2", totalNo2Count > 0 ? Math.round((totalNo2Sum / totalNo2Count) * 10.0) / 10.0 : null);
        overall.put("avgSo2", totalSo2Count > 0 ? Math.round((totalSo2Sum / totalSo2Count) * 10.0) / 10.0 : null);
        overall.put("avgCo", totalCoCount > 0 ? Math.round((totalCoSum / totalCoCount) * 100.0) / 100.0 : null);
        overall.put("dominantBucket", totalAqiCount > 0 ? deriveAqiBucket(totalAqiSum / totalAqiCount) : null);
        overall.put("totalReadings", records.size());

        aq.put("citySummaries", citySummaries);
        aq.put("aqiDistribution", bucketCounts);
        aq.put("overall", overall);

        Map<String, String> dateRange = new HashMap<>();
        dateRange.put("minDate", minDate);
        dateRange.put("maxDate", maxDate);
        aq.put("dateRange", dateRange);

        return aq;
    }

    private static class CityPollutionAggregator {
        final String cityName;
        int recordCount = 0;
        double aqiSum = 0;
        int aqiCount = 0;
        double pm25Sum = 0;
        int pm25Count = 0;
        double pm10Sum = 0;
        int pm10Count = 0;
        double no2Sum = 0;
        int no2Count = 0;
        double so2Sum = 0;
        int so2Count = 0;
        double coSum = 0;
        int coCount = 0;
        final Map<String, Integer> bucketCounts = new HashMap<>();

        CityPollutionAggregator(String cityName) {
            this.cityName = cityName;
        }
    }

    private String findColumn(Map<String, Object> row, String... aliases) {
        for (String k : row.keySet()) {
            String norm = normalizeKey(k);
            for (String alias : aliases) {
                if (norm.equals(normalizeKey(alias))) {
                    return k;
                }
            }
        }
        return null;
    }

    private String deriveAqiBucket(double aqi) {
        if (aqi <= 50) return "Good";
        if (aqi <= 100) return "Satisfactory";
        if (aqi <= 200) return "Moderate";
        if (aqi <= 300) return "Poor";
        if (aqi <= 400) return "Very Poor";
        return "Severe";
    }

    public Map<String, Object> loadCatalogDataset(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("data/" + fileName);
            if (!resource.exists()) {
                throw new IllegalArgumentException("Catalog dataset not found: " + fileName);
            }
            try (InputStream is = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return analyzeCsv(fileName, sb.toString(), null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load catalog dataset: " + e.getMessage(), e);
        }
    }

    private Map<String, String> detectGeographicFields(List<String> headers) {
        Map<String, String> detected = new LinkedHashMap<>();
        for (String h : headers) {
            String lower = h.toLowerCase().replaceAll("[^a-z0-9_]", "");
            if (detected.get("latitude") == null && (lower.equals("latitude") || lower.equals("lat") || lower.equals("point_latitude") || lower.equals("coord_lat") || lower.equals("y"))) {
                detected.put("latitude", h);
            }
            if (detected.get("longitude") == null && (lower.equals("longitude") || lower.equals("lon") || lower.equals("lng") || lower.equals("point_longitude") || lower.equals("coord_lng") || lower.equals("x"))) {
                detected.put("longitude", h);
            }
            if (detected.get("district") == null && (lower.contains("district") || lower.equals("dist"))) {
                detected.put("district", h);
            }
            if (detected.get("city") == null && (lower.equals("city") || lower.contains("city_name") || lower.contains("municipality"))) {
                detected.put("city", h);
            }
            if (detected.get("zone") == null && (lower.equals("zone") || lower.contains("zone_name") || lower.contains("ward") || lower.contains("locality"))) {
                detected.put("zone", h);
            }
            if (detected.get("locationId") == null && (lower.equals("location_id") || lower.equals("locationid") || lower.equals("loc_id") || lower.equals("zone_id") || lower.equals("id"))) {
                detected.put("locationId", h);
            }
        }
        return detected;
    }

    private boolean isColumnNumeric(List<Map<String, Object>> records, String header) {
        String clean = header.toLowerCase().replaceAll("[^a-z0-9_.]", "");
        if (KNOWN_CATEGORICAL_NAMES.contains(clean) || clean.contains("bucket") || clean.contains("category")) {
            return false;
        }

        int validNonMissing = 0;
        int numericCount = 0;
        for (Map<String, Object> r : records) {
            Object v = r.get(header);
            if (v != null) {
                String str = v.toString().trim();
                if (!isMissingValue(str)) {
                    validNonMissing++;
                    if (isNumber(str)) {
                        numericCount++;
                    }
                }
            }
        }

        if (validNonMissing > 0) {
            return ((double) numericCount / validNonMissing) >= 0.50;
        }

        // Schema fallback: If 100% of rows have missing values (e.g. empty CSV rows),
        // recognize known numeric metrics from header names!
        return KNOWN_AIR_QUALITY_METRICS.contains(clean) ||
                clean.startsWith("pm") || clean.startsWith("no") ||
                clean.equals("so2") || clean.equals("nh3") || clean.equals("co") ||
                clean.equals("o3") || clean.equals("aqi") ||
                clean.contains("speed") || clean.contains("delay") ||
                clean.contains("rate") || clean.contains("ratio") ||
                clean.contains("pct") || clean.contains("percent");
    }

    private boolean isMissingValue(String str) {
        if (str == null) return true;
        String trimmed = str.trim().toLowerCase();
        return trimmed.isEmpty() ||
                trimmed.equals("nan") ||
                trimmed.equals("na") ||
                trimmed.equals("n/a") ||
                trimmed.equals("null") ||
                trimmed.equals("none") ||
                trimmed.equals("nil") ||
                trimmed.equals("-") ||
                trimmed.equals("--");
    }

    private boolean isNumber(String str) {
        if (isMissingValue(str)) return false;
        try {
            Double.parseDouble(str.replace("%", "").replace(",", "").trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Double parseDouble(Object obj) {
        if (obj == null) return null;
        String str = obj.toString().trim();
        if (isMissingValue(str)) return null;
        try {
            str = str.replace("%", "").replace(",", "").trim();
            if (str.isEmpty()) return null;
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isValidCoordinate(double lat, double lng) {
        return lat >= -90.0 && lat <= 90.0 && lng >= -180.0 && lng <= 180.0;
    }

    private Map<String, Object> computeColumnStats(List<Map<String, Object>> records, String header) {
        Double min = null;
        Double max = null;
        double sum = 0.0;
        int count = 0;
        int missingCount = 0;

        for (Map<String, Object> r : records) {
            Double val = parseDouble(r.get(header));
            if (val != null) {
                if (min == null || val < min) min = val;
                if (max == null || val > max) max = val;
                sum += val;
                count++;
            } else {
                missingCount++;
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("min", min != null ? Math.round(min * 100.0) / 100.0 : null);
        stats.put("max", max != null ? Math.round(max * 100.0) / 100.0 : null);
        stats.put("avg", count > 0 ? Math.round((sum / count) * 100.0) / 100.0 : null);
        stats.put("count", count);
        stats.put("missingCount", missingCount);
        double missingPct = records.isEmpty() ? 0.0 : ((double) missingCount / records.size()) * 100.0;
        stats.put("missingPct", Math.round(missingPct * 10.0) / 10.0);
        stats.put("unit", deduceUnit(header));

        return stats;
    }

    private Map<String, Object> computeMissingStats(List<Map<String, Object>> records, String header) {
        int presentCount = 0;
        int missingCount = 0;

        for (Map<String, Object> r : records) {
            Object v = r.get(header);
            if (v != null && !isMissingValue(v.toString())) {
                presentCount++;
            } else {
                missingCount++;
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("presentCount", presentCount);
        stats.put("missingCount", missingCount);
        double missingPct = records.isEmpty() ? 0.0 : ((double) missingCount / records.size()) * 100.0;
        stats.put("missingPct", Math.round(missingPct * 10.0) / 10.0);
        return stats;
    }

    private String deduceUnit(String header) {
        String lower = header.toLowerCase();
        if (lower.contains("pm2.5") || lower.contains("pm25") || lower.contains("pm10") || lower.contains("nh3") || lower.contains("so2") || lower.contains("no2") || lower.contains("o3")) {
            return " µg/m³";
        } else if (lower.contains("co")) {
            return " mg/m³";
        } else if (lower.contains("benzene") || lower.contains("toluene") || lower.contains("xylene")) {
            return " µg/m³";
        } else if (lower.contains("aqi")) {
            return " AQI";
        } else if (lower.contains("pct") || lower.contains("ratio") || lower.contains("rate") || lower.contains("percent")) {
            return "%";
        } else if (lower.contains("kmh") || lower.contains("speed")) {
            return " km/h";
        } else if (lower.contains("min") || lower.contains("delay")) {
            return " min";
        } else if (lower.contains("temp") || lower.contains("°c")) {
            return " °C";
        } else if (lower.contains("mld")) {
            return " MLD";
        } else if (lower.contains("crore")) {
            return " ₹ Cr";
        } else if (lower.contains("ppm")) {
            return " ppm";
        }
        return "";
    }

    private List<List<String>> parseCsv(String content) {
        List<List<String>> rows = new ArrayList<>();
        if (content == null || content.isEmpty()) return rows;

        List<String> currentRow = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;

        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (insideQuotes) {
                if (c == '"') {
                    if (i + 1 < chars.length && chars[i + 1] == '"') {
                        currentField.append('"');
                        i++; // skip escaped quote
                    } else {
                        insideQuotes = false;
                    }
                } else {
                    currentField.append(c);
                }
            } else {
                if (c == '"') {
                    insideQuotes = true;
                } else if (c == ',' || c == '\t') {
                    currentRow.add(currentField.toString());
                    currentField.setLength(0);
                } else if (c == '\r') {
                    if (i + 1 < chars.length && chars[i + 1] == '\n') {
                        i++;
                    }
                    currentRow.add(currentField.toString());
                    currentField.setLength(0);
                    rows.add(new ArrayList<>(currentRow));
                    currentRow.clear();
                } else if (c == '\n') {
                    currentRow.add(currentField.toString());
                    currentField.setLength(0);
                    rows.add(new ArrayList<>(currentRow));
                    currentRow.clear();
                } else {
                    currentField.append(c);
                }
            }
        }

        if (currentField.length() > 0 || !currentRow.isEmpty()) {
            currentRow.add(currentField.toString());
            rows.add(currentRow);
        }

        return rows;
    }
}
