package org.database;

import org.dtos.VehicleInfo;
import org.enums.VehicleType;

import java.util.Collection;

public interface DbAccessPoint {
    void saveVehicle(VehicleInfo vehicle);

    void updateVehicle(VehicleInfo vehicle);
}
