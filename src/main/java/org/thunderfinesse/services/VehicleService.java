package org.thunderfinesse.services;

import org.dtos.VehicleStats;
import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import java.util.List;

public interface VehicleService {
    void update();
    List<VehicleStats> getVehicleStats(String vehicleId);
}
