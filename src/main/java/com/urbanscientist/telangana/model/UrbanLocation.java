package com.urbanscientist.telangana.model;

import jakarta.persistence.*;

@Entity
@Table(name = "locations", indexes = {
    @Index(name = "idx_loc_district", columnList = "district"),
    @Index(name = "idx_loc_city", columnList = "city"),
    @Index(name = "idx_loc_id", columnList = "locationId")
})
public class UrbanLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String state = "Telangana";

    @Column(nullable = false)
    private String stateId = "TS";

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String districtId;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String cityId;

    @Column(nullable = false)
    private String zone;

    @Column(nullable = false)
    private String zoneId;

    @Column(nullable = false, unique = true)
    private String locationId;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Double areaSqKm;
    private Double elevationM;
    private String urbanCategory;
    private String dataType = "SYNTHETIC_DEMO";

    public UrbanLocation() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getDistrictId() { return districtId; }
    public void setDistrictId(String districtId) { this.districtId = districtId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCityId() { return cityId; }
    public void setCityId(String cityId) { this.cityId = cityId; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getAreaSqKm() { return areaSqKm; }
    public void setAreaSqKm(Double areaSqKm) { this.areaSqKm = areaSqKm; }

    public Double getElevationM() { return elevationM; }
    public void setElevationM(Double elevationM) { this.elevationM = elevationM; }

    public String getUrbanCategory() { return urbanCategory; }
    public void setUrbanCategory(String urbanCategory) { this.urbanCategory = urbanCategory; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}
