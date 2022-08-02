package org.database;

import org.dtos.VehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import java.util.List;

public interface GameStatsAccessPoint {
    void saveVehicleStat(VehicleStats vehicle);
    void updateVehicleStat(VehicleStats vehicle);
    void upsertVehicleStat(VehicleStats vehicle);
    List<VehicleStats> getVehicleStat(String vehicleId);
    List<VehicleStats> getVehiclesStats(Modes mode, VehicleType type);

}
