package com.urbanscientist.telangana.dto;

public class SimulationRequest {

    private String district;
    private String city;
    private String zone;
    private String interventionId;

    public SimulationRequest() {}

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getInterventionId() { return interventionId; }
    public void setInterventionId(String interventionId) { this.interventionId = interventionId; }
}
