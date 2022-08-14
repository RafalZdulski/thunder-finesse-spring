package org.thunderfinesse.services;

import org.database.dtos.VehicleStats;
import org.dtos.VehicleStatsResponse;
import org.enums.Modes;
import org.enums.VehicleType;

import java.util.List;

public interface VehicleService {
    void update();
    VehicleStatsResponse getVehicleStats(String vehicleId);
    List<VehicleStatsResponse> getVehiclesStats(Modes mode, VehicleType type);
}
