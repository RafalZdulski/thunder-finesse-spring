package org.thunderwiki;

import org.database.dtos.VehicleInfo;
import org.enums.VehicleType;

import java.io.IOException;

public interface VehicleScrapper {
    VehicleInfo parseVehicle(VehicleType type, String url) throws IOException;

}
