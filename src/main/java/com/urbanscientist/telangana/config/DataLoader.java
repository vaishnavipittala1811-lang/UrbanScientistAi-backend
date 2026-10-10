package com.urbanscientist.telangana.config;

import com.urbanscientist.telangana.model.*;
import com.urbanscientist.telangana.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

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

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedUrbanData();
        seedInterventions();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            userRepository.save(new User("Dr. K. Srinivas (Chief Urban Officer)", "admin@urbanscientist.telangana.gov.in", passwordEncoder.encode("Admin@123"), Role.ADMIN));
            userRepository.save(new User("Pooja Reddy (Senior City Planner)", "planner@urbanscientist.telangana.gov.in", passwordEncoder.encode("Planner@123"), Role.PLANNER));
            userRepository.save(new User("Venkat Raman (Spatial Data Analyst)", "analyst@urbanscientist.telangana.gov.in", passwordEncoder.encode("Analyst@123"), Role.ANALYST));
            userRepository.save(new User("Telangana Civic Observer", "viewer@urbanscientist.telangana.gov.in", passwordEncoder.encode("Viewer@123"), Role.VIEWER));
            System.out.println(">>> Seeded 4 default authentication roles for Telangana Urban Intelligence platform.");
        }
    }

    private static class DistDef {
        String district, districtId, category;
        double lat, lng;
        List<CityDef> cities;

        DistDef(String d, String dId, String cat, double la, double ln, List<CityDef> c) {
            district = d; districtId = dId; category = cat; lat = la; lng = ln; cities = c;
        }
    }

    private static class CityDef {
        String city, cityId;
        double lat, lng;
        List<String> zones;

        CityDef(String c, String cId, double la, double ln, List<String> z) {
            city = c; cityId = cId; lat = la; lng = ln; zones = z;
        }
    }

    private void seedUrbanData() {
        if (locationRepository.count() > 0) {
            return;
        }

        List<DistDef> dists = List.of(
            new DistDef("Hyderabad", "TS-HYD", "Mega Metro", 17.3850, 78.4867, List.of(
                new CityDef("Hyderabad Central", "TS-HYD-01", 17.3920, 78.4730, List.of("Charminar Zone", "Khairatabad Zone", "Secunderabad Zone")),
                new CityDef("Cyberabad", "TS-HYD-02", 17.4435, 78.3772, List.of("Madhapur IT Corridor", "Gachibowli Financial District")),
                new CityDef("North Hyderabad", "TS-HYD-03", 17.4849, 78.4138, List.of("Kukatpally Zone", "Miyapur Metro Zone"))
            )),
            new DistDef("Warangal", "TS-WGL", "Major Urban City", 17.9689, 79.5941, List.of(
                new CityDef("Warangal City", "TS-WGL-01", 17.9784, 79.6000, List.of("Fort Warangal Heritage Zone", "Narsampet Corridor"))
            )),
            new DistDef("Hanamkonda", "TS-HNK", "Major Urban City", 18.0138, 79.5492, List.of(
                new CityDef("Hanamkonda Urban", "TS-HNK-01", 18.0150, 79.5510, List.of("Subedari Administrative Zone", "Kazipet Junction Node"))
            )),
            new DistDef("Karimnagar", "TS-KNR", "Major Urban City", 18.4386, 79.1288, List.of(
                new CityDef("Karimnagar Urban", "TS-KNR-01", 18.4390, 79.1300, List.of("Collectorate Zone", "Manakondur Highway Zone"))
            )),
            new DistDef("Nizamabad", "TS-NZB", "Major Urban City", 18.6725, 78.0941, List.of(
                new CityDef("Nizamabad City", "TS-NZB-01", 18.6730, 78.0950, List.of("Khaleelwadi Commercial Hub", "Bodhan Road Zone"))
            )),
            new DistDef("Khammam", "TS-KMM", "Major Urban City", 17.2473, 80.1514, List.of(
                new CityDef("Khammam Municipality", "TS-KMM-01", 17.2500, 80.1500, List.of("Fort Zone", "Wyra Road Node"))
            )),
            new DistDef("Nalgonda", "TS-NLG", "Major Urban City", 17.0575, 79.2684, List.of(
                new CityDef("Nalgonda Urban", "TS-NLG-01", 17.0600, 79.2700, List.of("Clock Tower Center", "Miryalaguda Bypass Zone"))
            )),
            new DistDef("Siddipet", "TS-SDP", "Growing Municipality", 18.1018, 78.8520, List.of(
                new CityDef("Siddipet Model Town", "TS-SDP-01", 18.1050, 78.8550, List.of("Komati Cheruvu Eco-Zone", "Gajwel Civic Node"))
            )),
            new DistDef("Sangareddy", "TS-SRD", "Industrial Hub", 17.6140, 78.0816, List.of(
                new CityDef("Sangareddy-Patancheru", "TS-SRD-01", 17.5300, 78.2600, List.of("Patancheru Industrial Belt", "IIT Kandi Knowledge Hub"))
            )),
            new DistDef("Medchal-Malkajgiri", "TS-MDM", "Mega Metro", 17.4988, 78.5833, List.of(
                new CityDef("Medchal-Kompally", "TS-MDM-01", 17.6200, 78.4800, List.of("Kompally Logistics Zone", "Uppal IT & Industrial Park"))
            )),
            new DistDef("Rangareddy", "TS-RRD", "Mega Metro", 17.3117, 78.4069, List.of(
                new CityDef("Shamshabad Outer", "TS-RRD-01", 17.2403, 78.4294, List.of("Airport Aerotropolis", "Maheshwaram Pharma City"))
            )),
            new DistDef("Bhadradri Kothagudem", "TS-BDK", "Industrial Hub", 17.5500, 80.6167, List.of(
                new CityDef("Kothagudem-Palwancha", "TS-BDK-01", 17.5600, 80.6200, List.of("Mining Operations Belt", "Palwancha Thermal Power Zone"))
            )),
            new DistDef("Peddapalli", "TS-PDP", "Industrial Hub", 18.6161, 79.3831, List.of(
                new CityDef("Ramagundam Industrial City", "TS-PDP-01", 18.7600, 79.4800, List.of("NTPC Power Corridor", "Godavarikhani Coal Node"))
            )),
            new DistDef("Rajanna Sircilla", "TS-RJS", "Growing Municipality", 18.3844, 78.8353, List.of(
                new CityDef("Sircilla Textile City", "TS-RJS-01", 18.3900, 78.8400, List.of("Textile Park Zone", "Vemulawada Temple Node"))
            )),
            new DistDef("Suryapet", "TS-SYP", "Major Urban City", 17.1439, 79.6236, List.of(
                new CityDef("Suryapet City", "TS-SYP-01", 17.1500, 79.6300, List.of("Highway Commercial Corridor", "Kodad Gateway"))
            )),
            new DistDef("Mahbubnagar", "TS-MBN", "Major Urban City", 16.7488, 77.9856, List.of(
                new CityDef("Mahbubnagar Urban", "TS-MBN-01", 16.7500, 77.9900, List.of("Pillalamarri Heritage Zone", "Jadcherla Industrial Corridor"))
            )),
            new DistDef("Mancherial", "TS-MNCL", "Industrial Hub", 18.8711, 79.4639, List.of(
                new CityDef("Mancherial Urban", "TS-MNCL-01", 18.8800, 79.4700, List.of("Godavari Riverfront Zone", "Bellampalli Node"))
            )),
            new DistDef("Jagtial", "TS-JGT", "Growing Municipality", 18.7947, 78.9126, List.of(
                new CityDef("Jagtial Urban", "TS-JGT-01", 18.8000, 78.9200, List.of("Tower Circle Zone", "Koratla Node"))
            )),
            new DistDef("Jangaon", "TS-JGN", "Growing Municipality", 17.7231, 79.1558, List.of(
                new CityDef("Jangaon Town", "TS-JGN-01", 17.7250, 79.1600, List.of("Station Ghanpur Zone", "Old Town Civic Zone"))
            )),
            new DistDef("Jayashankar Bhupalpally", "TS-JSB", "Industrial Hub", 18.4314, 79.8647, List.of(
                new CityDef("Bhupalpally", "TS-JSB-01", 18.4400, 79.8700, List.of("Coalfield Belt Zone", "Kataram Node"))
            )),
            new DistDef("Jogulamba Gadwal", "TS-JGD", "Growing Municipality", 16.2327, 77.8078, List.of(
                new CityDef("Gadwal Town", "TS-JGD-01", 16.2400, 77.8100, List.of("Fort Area Zone", "Alampur Heritage Zone"))
            )),
            new DistDef("Kamareddy", "TS-KMR", "Growing Municipality", 18.3225, 78.3392, List.of(
                new CityDef("Kamareddy Urban", "TS-KMR-01", 18.3300, 78.3400, List.of("Highway Gateway Zone", "Banswada Node"))
            )),
            new DistDef("Komaram Bheem Asifabad", "TS-KBA", "Growing Municipality", 19.3583, 79.2833, List.of(
                new CityDef("Asifabad-Kagaznagar", "TS-KBA-01", 19.3600, 79.3000, List.of("Kagaznagar Industrial Node", "Asifabad Central"))
            )),
            new DistDef("Mahabubabad", "TS-MBD", "Growing Municipality", 17.5986, 80.0039, List.of(
                new CityDef("Mahabubabad Town", "TS-MBD-01", 17.6000, 80.0100, List.of("Railway Junction Zone", "Dornakal Node"))
            )),
            new DistDef("Medak", "TS-MDK", "Growing Municipality", 18.0461, 78.2614, List.of(
                new CityDef("Medak Heritage Town", "TS-MDK-01", 18.0500, 78.2700, List.of("Cathedral Heritage Zone", "Narsapur Forest Margin"))
            )),
            new DistDef("Mulugu", "TS-MLG", "Growing Municipality", 18.1903, 79.9431, List.of(
                new CityDef("Mulugu Eco Hub", "TS-MLG-01", 18.2000, 79.9500, List.of("Ramappa UNESCO Heritage Zone", "Medaram Eco-Belt"))
            )),
            new DistDef("Nagarkurnool", "TS-NGK", "Growing Municipality", 16.4856, 78.3328, List.of(
                new CityDef("Nagarkurnool Town", "TS-NGK-01", 16.4900, 78.3400, List.of("Kalwakurthy Corridor", "Town Center"))
            )),
            new DistDef("Narayanpet", "TS-NRP", "Growing Municipality", 16.7369, 77.4981, List.of(
                new CityDef("Narayanpet Handloom Hub", "TS-NRP-01", 16.7400, 77.5000, List.of("Weavers Cluster Zone", "Makthal Node"))
            )),
            new DistDef("Nirmal", "TS-NRM", "Growing Municipality", 19.0964, 78.3431, List.of(
                new CityDef("Nirmal Craft Town", "TS-NRM-01", 19.1000, 78.3500, List.of("Artisan Cluster Zone", "Bhainsa Border Node"))
            )),
            new DistDef("Vikarabad", "TS-VKB", "Growing Municipality", 17.3364, 77.9044, List.of(
                new CityDef("Vikarabad Hill Station", "TS-VKB-01", 17.3400, 77.9100, List.of("Ananthagiri Hills Eco-Zone", "Tandur Cement Corridor"))
            )),
            new DistDef("Wanaparthy", "TS-WNP", "Growing Municipality", 16.3622, 78.0617, List.of(
                new CityDef("Wanaparthy Town", "TS-WNP-01", 16.3700, 78.0700, List.of("Palace Heritage Zone", "Pebbair Highway Node"))
            )),
            new DistDef("Yadadri Bhuvanagiri", "TS-YDB", "Growing Municipality", 17.5111, 78.8833, List.of(
                new CityDef("Bhuvanagiri Temple City", "TS-YDB-01", 17.5200, 78.8900, List.of("Yadagirigutta Temple City", "Choutuppal Industrial Cluster"))
            )),
            new DistDef("Adilabad", "TS-ADB", "Major Urban City", 19.6641, 78.5320, List.of(
                new CityDef("Adilabad Urban", "TS-ADB-01", 19.6700, 78.5400, List.of("Cotton Market Trading Zone", "Bela Border Node"))
            ))
        );

        Random rnd = new Random(101);
        int locCounter = 1;

        for (DistDef dd : dists) {
            double weight = "Mega Metro".equals(dd.category) ? 1.6 : ("Major Urban City".equals(dd.category) ? 1.2 : ("Industrial Hub".equals(dd.category) ? 1.3 : 0.85));

            for (CityDef cd : dd.cities) {
                int zIdx = 1;
                for (String zoneName : cd.zones) {
                    String locId = String.format("TS-LOC-%03d", locCounter);
                    String zoneId = String.format("%s-Z%d", cd.cityId, zIdx++);

                    double latJitter = (rnd.nextDouble() - 0.5) * 0.04;
                    double lngJitter = (rnd.nextDouble() - 0.5) * 0.04;
                    double zLat = Math.round((cd.lat + latJitter) * 10000.0) / 10000.0;
                    double zLng = Math.round((cd.lng + lngJitter) * 10000.0) / 10000.0;

                    UrbanLocation loc = new UrbanLocation();
                    loc.setState("Telangana");
                    loc.setStateId("TS");
                    loc.setDistrict(dd.district);
                    loc.setDistrictId(dd.districtId);
                    loc.setCity(cd.city);
                    loc.setCityId(cd.cityId);
                    loc.setZone(zoneName);
                    loc.setZoneId(zoneId);
                    loc.setLocationId(locId);
                    loc.setLocationName(zoneName + ", " + cd.city);
                    loc.setLatitude(zLat);
                    loc.setLongitude(zLng);
                    loc.setAreaSqKm(Math.round((18.0 + rnd.nextDouble() * 25.0) * 10.0) / 10.0);
                    loc.setElevationM(Math.round((320.0 + rnd.nextDouble() * 210.0) * 10.0) / 10.0);
                    loc.setUrbanCategory(dd.category);
                    loc.setDataType("SYNTHETIC_DEMO");
                    locationRepository.save(loc);

                    // Traffic
                    double avgSpd = Math.round((Math.max(16.0, (28.0 + rnd.nextDouble() * 25.0) / (weight * 0.9))) * 10.0) / 10.0;
                    double cong = Math.round((Math.min(95.0, (32.0 + rnd.nextDouble() * 30.0) * weight)) * 10.0) / 10.0;
                    double trfRisk = Math.round((cong * 0.6 + (60.0 - avgSpd) * 0.4) * 10.0) / 10.0;

                    TrafficRecord trf = new TrafficRecord();
                    trf.setLocationId(locId);
                    trf.setState("Telangana");
                    trf.setDistrict(dd.district);
                    trf.setCity(cd.city);
                    trf.setZone(zoneName);
                    trf.setAvgSpeedKmh(avgSpd);
                    trf.setCongestionLevelPct(cong);
                    trf.setPeakHourDelayMin((int) (cong * 0.6 + rnd.nextInt(12)));
                    trf.setDailyVehicleCount((int) ((30000 + rnd.nextInt(90000)) * weight));
                    trf.setRoadNetworkDensity(Math.round((5.0 + rnd.nextDouble() * 8.0 * weight) * 10.0) / 10.0);
                    trf.setIncidentFrequencyWeekly((int) (trfRisk / 12 + rnd.nextInt(3)));
                    trf.setPublicTransitCoveragePct(Math.round(Math.max(25.0, 88.0 - trfRisk * 0.4) * 10.0) / 10.0);
                    trf.setTrafficRiskScore(trfRisk);
                    trf.setHotspotFlag(trfRisk > 70.0 ? 1 : 0);
                    trf.setTimestamp("2026-10-05 18:30:00");
                    trf.setDataType("SYNTHETIC_DEMO");
                    trafficRepository.save(trf);

                    // Pollution
                    double pm25 = Math.round((Math.max(18.0, 30.0 * weight + ("Industrial Hub".equals(dd.category) ? 22.0 : 0.0) + (rnd.nextDouble() - 0.4) * 16.0)) * 10.0) / 10.0;
                    double pm10 = Math.round((pm25 * (1.6 + rnd.nextDouble() * 0.5)) * 10.0) / 10.0;
                    double no2 = Math.round(((20.0 + rnd.nextDouble() * 35.0) * weight) * 10.0) / 10.0;
                    int aqi = (int) (pm25 * 1.8 + no2 * 0.3);
                    String aqiCat = aqi <= 50 ? "Good" : (aqi <= 100 ? "Satisfactory" : (aqi <= 200 ? "Moderate" : (aqi <= 300 ? "Poor" : "Very Poor")));
                    double polRisk = Math.round(Math.min(98.0, (aqi / 280.0) * 100.0) * 10.0) / 10.0;

                    PollutionRecord pol = new PollutionRecord();
                    pol.setLocationId(locId);
                    pol.setState("Telangana");
                    pol.setDistrict(dd.district);
                    pol.setCity(cd.city);
                    pol.setZone(zoneName);
                    pol.setPm25(pm25);
                    pol.setPm10(pm10);
                    pol.setNo2(no2);
                    pol.setSo2(Math.round((8.0 + rnd.nextDouble() * 20.0) * 10.0) / 10.0);
                    pol.setCo(Math.round((0.8 + rnd.nextDouble() * 1.8) * 10.0) / 10.0);
                    pol.setAqi(aqi);
                    pol.setAqiCategory(aqiCat);
                    pol.setIndustrialEmissionIndex(Math.round(("Industrial Hub".equals(dd.category) ? 65.0 + rnd.nextDouble() * 25.0 : 25.0 + rnd.nextDouble() * 35.0) * 10.0) / 10.0);
                    pol.setPollutionRiskScore(polRisk);
                    pol.setDominantPollutant(pm25 > 50 ? "PM2.5" : "NO2");
                    pol.setTimestamp("2026-10-05 18:30:00");
                    pol.setDataType("SYNTHETIC_DEMO");
                    pollutionRepository.save(pol);

                    // Water
                    double gwDepth = Math.round((10.0 + rnd.nextDouble() * 24.0 * (weight > 1.2 ? 1.25 : 0.95)) * 10.0) / 10.0;
                    double gwStress = Math.round(Math.min(95.0, (gwDepth / 32.0) * 100.0) * 10.0) / 10.0;
                    double wDeficit = Math.round(Math.max(3.0, Math.min(42.0, gwStress * 0.4 - 5.0 + rnd.nextDouble() * 10.0)) * 10.0) / 10.0;
                    double wDemand = Math.round((50.0 + rnd.nextDouble() * 150.0 * weight) * 10.0) / 10.0;
                    double wSupply = Math.round((wDemand * (1.0 - wDeficit / 100.0)) * 10.0) / 10.0;
                    double wqi = Math.round((88.0 - polRisk * 0.28 + rnd.nextDouble() * 8.0) * 10.0) / 10.0;
                    double wRisk = Math.round((gwStress * 0.45 + wDeficit * 0.35 + (100.0 - wqi) * 0.2) * 10.0) / 10.0;

                    WaterRecord wtr = new WaterRecord();
                    wtr.setLocationId(locId);
                    wtr.setState("Telangana");
                    wtr.setDistrict(dd.district);
                    wtr.setCity(cd.city);
                    wtr.setZone(zoneName);
                    wtr.setGroundwaterDepthM(gwDepth);
                    wtr.setGroundwaterStressIndex(gwStress);
                    wtr.setPipedWaterCoveragePct(Math.round(Math.max(45.0, 92.0 - gwStress * 0.35) * 10.0) / 10.0);
                    wtr.setDailyWaterDemandMld(wDemand);
                    wtr.setDailyWaterSupplyMld(wSupply);
                    wtr.setSupplyDeficitPct(wDeficit);
                    wtr.setWaterQualityIndex(wqi);
                    wtr.setTdsPpm(250 + rnd.nextInt(500));
                    wtr.setWaterRiskScore(wRisk);
                    wtr.setFloodVulnerabilityIndex(Math.round(("Hyderabad".equals(dd.district) || "Warangal".equals(dd.district) ? 55.0 + rnd.nextDouble() * 32.0 : 20.0 + rnd.nextDouble() * 30.0) * 10.0) / 10.0);
                    wtr.setTimestamp("2026-10-05 18:30:00");
                    wtr.setDataType("SYNTHETIC_DEMO");
                    waterRepository.save(wtr);

                    // Weather
                    double temp = Math.round((30.0 + rnd.nextDouble() * 6.0) * 10.0) / 10.0;
                    double uhi = Math.round((1.5 + rnd.nextDouble() * 3.8 * (weight > 1.2 ? 1.3 : 0.8)) * 10.0) / 10.0;
                    WeatherRecord wth = new WeatherRecord();
                    wth.setLocationId(locId);
                    wth.setState("Telangana");
                    wth.setDistrict(dd.district);
                    wth.setCity(cd.city);
                    wth.setZone(zoneName);
                    wth.setTemperatureC(temp);
                    wth.setHeatIndexC(Math.round((temp + uhi * 0.75 + 1.2) * 10.0) / 10.0);
                    wth.setHumidityPct(Math.round((45.0 + rnd.nextDouble() * 30.0) * 10.0) / 10.0);
                    wth.setRainfallMmMonthly(Math.round((60.0 + rnd.nextDouble() * 90.0) * 10.0) / 10.0);
                    wth.setWindSpeedKmh(Math.round((10.0 + rnd.nextDouble() * 14.0) * 10.0) / 10.0);
                    wth.setSurfaceHeatIslandIntensityC(uhi);
                    wth.setMonsoonFloodRiskPct(Math.round((25.0 + rnd.nextDouble() * 45.0) * 10.0) / 10.0);
                    wth.setDroughtRiskPct(Math.round((gwStress * 0.55 + (40.0 - temp) * 0.3) * 10.0) / 10.0);
                    wth.setTimestamp("2026-10-05 18:30:00");
                    wth.setDataType("SYNTHETIC_DEMO");
                    weatherRepository.save(wth);

                    // Population
                    long basePop = (long) ((65000 + rnd.nextInt(180000)) * Math.pow(weight, 1.3));
                    int density = (int) (basePop / loc.getAreaSqKm());
                    double growth = Math.round((2.0 + rnd.nextDouble() * 2.2 * (weight > 1.2 ? 1.2 : 0.9)) * 100.0) / 100.0;
                    long f5 = (long) (basePop * Math.pow(1.0 + growth / 100.0, 5));
                    long f10 = (long) (basePop * Math.pow(1.0 + growth / 100.0, 10));
                    long f15 = (long) (basePop * Math.pow(1.0 + growth / 100.0, 15));
                    double popPres = Math.round(Math.min(95.0, (density / 16000.0) * 100.0) * 10.0) / 10.0;

                    PopulationRecord pop = new PopulationRecord();
                    pop.setLocationId(locId);
                    pop.setState("Telangana");
                    pop.setDistrict(dd.district);
                    pop.setCity(cd.city);
                    pop.setZone(zoneName);
                    pop.setCurrentPopulation(basePop);
                    pop.setPopulationDensityPerSqkm(density);
                    pop.setAnnualGrowthRatePct(growth);
                    pop.setUrbanizationRatePct(Math.round(Math.min(96.0, 62.0 * weight) * 10.0) / 10.0);
                    pop.setForecast5yr(f5);
                    pop.setForecast10yr(f10);
                    pop.setForecast15yr(f15);
                    pop.setMigrantInfluxRate(Math.round((1.5 + rnd.nextDouble() * 4.0 * weight) * 100.0) / 100.0);
                    pop.setDependencyRatio(Math.round((0.38 + rnd.nextDouble() * 0.16) * 100.0) / 100.0);
                    pop.setPopulationPressureScore(popPres);
                    pop.setDataType("SYNTHETIC_DEMO");
                    populationRepository.save(pop);

                    // Healthcare
                    double beds = Math.round((1.4 + rnd.nextDouble() * 2.4 * (weight > 1.2 ? 1.2 : 0.8)) * 10.0) / 10.0;
                    double icu = Math.round((60.0 + rnd.nextDouble() * 34.0) * 10.0) / 10.0;
                    double amb = Math.round((10.0 + rnd.nextDouble() * 16.0 * (cong > 65 ? 1.25 : 0.9)) * 10.0) / 10.0;
                    double hcPres = Math.round(Math.min(95.0, icu * 0.4 + (4.0 - beds) * 14.0 + amb * 1.4) * 10.0) / 10.0;

                    HealthcareRecord hc = new HealthcareRecord();
                    hc.setLocationId(locId);
                    hc.setState("Telangana");
                    hc.setDistrict(dd.district);
                    hc.setCity(cd.city);
                    hc.setZone(zoneName);
                    hc.setHospitalBedsPer1000(beds);
                    hc.setPrimaryHealthCenters(3 + rnd.nextInt(9));
                    hc.setIcuOccupancyRatePct(icu);
                    hc.setDoctorToPopRatio(Math.round((0.6 + rnd.nextDouble() * 1.2) * 100.0) / 100.0);
                    hc.setAmbulanceAvgResponseMin(amb);
                    hc.setMaternalHealthCoveragePct(Math.round((82.0 + rnd.nextDouble() * 16.0) * 10.0) / 10.0);
                    hc.setEpidemicVulnerabilityIndex(Math.round((popPres * 0.4 + polRisk * 0.35 + (100.0 - beds * 20.0) * 0.25) * 10.0) / 10.0);
                    hc.setHealthcarePressureScore(hcPres);
                    hc.setCriticalShortageFlag(icu > 85.0 || beds < 1.6 ? 1 : 0);
                    hc.setDataType("SYNTHETIC_DEMO");
                    healthcareRepository.save(hc);

                    // Infrastructure
                    double pwr = Math.round((88.0 + rnd.nextDouble() * 11.2) * 10.0) / 10.0;
                    double sew = Math.round((42.0 + rnd.nextDouble() * 52.0 * (weight > 1.2 ? 1.1 : 0.75)) * 10.0) / 10.0;
                    double pci = Math.round((45.0 + rnd.nextDouble() * 42.0) * 10.0) / 10.0;
                    double infStress = Math.round(Math.min(95.0, (100.0 - sew) * 0.35 + (100.0 - pci) * 0.35 + (100.0 - pwr) * 0.3) * 10.0) / 10.0;

                    InfrastructureRecord inf = new InfrastructureRecord();
                    inf.setLocationId(locId);
                    inf.setState("Telangana");
                    inf.setDistrict(dd.district);
                    inf.setCity(cd.city);
                    inf.setZone(zoneName);
                    inf.setPowerGridReliabilityPct(pwr);
                    inf.setSewageNetworkCoveragePct(sew);
                    inf.setSolidWasteCollectedTpd(Math.round((basePop * 0.45 / 1000.0) * 10.0) / 10.0);
                    inf.setWasteProcessingEfficiencyPct(Math.round((60.0 + rnd.nextDouble() * 32.0) * 10.0) / 10.0);
                    inf.setGreenCoverRatioPct(Math.round(Math.max(10.0, 36.0 / weight + rnd.nextDouble() * 8.0) * 10.0) / 10.0);
                    inf.setRoadQualityPci(pci);
                    inf.setInfrastructureStressScore(infStress);
                    inf.setRepairPriorityRank(infStress > 65.0 ? "High" : (infStress > 42.0 ? "Medium" : "Routine"));
                    inf.setDataType("SYNTHETIC_DEMO");
                    infrastructureRepository.save(inf);

                    locCounter++;
                }
            }
        }
        System.out.println(">>> Successfully seeded " + (locCounter - 1) + " Telangana urban locations across all 33 districts into MySQL.");
    }

    private void seedInterventions() {
        if (interventionRepository.count() > 0) return;

        List<Intervention> list = List.of(
            createInt("TS-INT-001", "Adaptive AI Traffic Signal & Corridor Synchronization", "Traffic",
                    "Deploy adaptive radar/camera signal controllers at key arterial intersections to cut congestion and idling emissions across Telangana urban corridors.",
                    34.5, 8, 28.5, 14200L, 850000L, 3.4, "High", "City"),

            createInt("TS-INT-002", "Urban Micro-Forests & Cool Roof Mandate", "Pollution",
                    "Establish Miyawaki micro-forests along ring roads and mandate high-albedo cool roofs on commercial/governmental buildings to combat UHI.",
                    18.0, 14, 22.0, 8900L, 420000L, 2.8, "High", "Zone"),

            createInt("TS-INT-003", "Mission Bhagiratha Urban Smart Water Grid & Leakage Telemetry", "Water",
                    "Install ultrasonic flow sensors and pressure monitoring across municipal distribution networks to cut non-revenue water and equalize supply duration.",
                    62.0, 18, 31.0, 3400L, 1250000L, 4.1, "Critical", "District"),

            createInt("TS-INT-004", "TSRTC Electric Feeder Bus Fleet & Micro-Mobility Corridors", "Traffic",
                    "Introduce 300 electric mini-feeders connecting metro/transit hubs with suburban residential nodes in Hyderabad, Warangal, and Nizamabad.",
                    115.0, 12, 26.0, 28500L, 1600000L, 3.1, "High", "District"),

            createInt("TS-INT-005", "Basti Dawakhana & Mobile Diagnostic Tele-Clinics Expansion", "Healthcare",
                    "Establish 120 new neighborhood urban primary clinics with real-time digital telemedicine access to major teaching hospitals.",
                    42.0, 10, 35.0, 1100L, 920000L, 4.5, "Critical", "City"),

            createInt("TS-INT-006", "Industrial Effluent & Dust Suppression Scrubbers Network", "Pollution",
                    "Deploy automated mist-cannons, continuous ambient PM sensors, and centralized effluent monitoring across industrial zones (Patancheru, Ramagundam, Kothagudem).",
                    29.5, 9, 24.0, 16400L, 580000L, 2.9, "High", "Zone"),

            createInt("TS-INT-007", "Sponge City Permeable Pavements & Lake Interlinking", "Infrastructure",
                    "Construct permeable road shoulders, bio-swales, and restore cascading chain-tank storm drainage to eliminate monsoon flash-flood stagnation.",
                    88.0, 20, 33.0, 5200L, 1400000L, 3.7, "Critical", "District"),

            createInt("TS-INT-008", "Municipal Solid Waste Automated Segregation & Bio-CNG Plant", "Infrastructure",
                    "Commission state-of-the-art decentralized waste-to-energy and bio-CNG generation facilities reducing landfill leaching.",
                    54.0, 15, 27.5, 31000L, 760000L, 3.3, "Medium", "City")
        );

        interventionRepository.saveAll(list);
        System.out.println(">>> Seeded 8 multi-sector urban interventions into MySQL.");
    }

    private Intervention createInt(String id, String name, String cat, String desc, double cost, int time, double red, Long co2, Long pop, double roi, String prio, String scope) {
        Intervention i = new Intervention();
        i.setInterventionId(id);
        i.setName(name);
        i.setCategory(cat);
        i.setDescription(desc);
        i.setCostInrCrores(cost);
        i.setImplementationTimeMonths(time);
        i.setExpectedRiskReductionPct(red);
        i.setCo2ReductionTonsYr(co2);
        i.setEstPopulationBenefited(pop);
        i.setRoiMultiplier(roi);
        i.setPriorityLevel(prio);
        i.setApplicableScope(scope);
        i.setDataType("SYNTHETIC_DEMO");
        return i;
    }
}
