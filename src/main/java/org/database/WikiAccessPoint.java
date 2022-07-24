package org.database;

import org.dtos.VehicleInfo;

public interface WikiAccessPoint {
    void saveVehicle(VehicleInfo vehicle);

    void updateVehicle(VehicleInfo vehicle);
}
