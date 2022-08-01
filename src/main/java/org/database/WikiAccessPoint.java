package org.database;

import org.dtos.VehicleInfo;
import org.enums.Modes;
import org.enums.VehicleType;

import java.util.List;

public interface WikiAccessPoint {
    void saveVehicle(VehicleInfo vehicle);

    void updateVehicle(VehicleInfo vehicle);

    List<String> getVehiclesIds(VehicleType type);
}
