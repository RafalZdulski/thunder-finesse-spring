package org.database;

import org.database.dtos.VehicleInfo;
import org.enums.VehicleType;

import java.util.List;

public interface WikiAccessPoint {
    void saveVehicle(VehicleInfo vehicle);

    void updateVehicle(VehicleInfo vehicle);

    List<String> getVehiclesIds(VehicleType type);
}
