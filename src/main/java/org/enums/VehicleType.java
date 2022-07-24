package org.enums;

public enum VehicleType {
    Aircraft("aircraft"),
    GroundVehicle("ground_vehicles"),
    Helicopter("helicopters"),
    BluewaterVessel("Bluewater_Fleet"),
    CoastalVessel("Coastal_Fleet");

    private String wikiPath;

    VehicleType(String wikiPath) {
        this.wikiPath = wikiPath;
    }

    public String getWikiPath() {
        return wikiPath;
    }
}
