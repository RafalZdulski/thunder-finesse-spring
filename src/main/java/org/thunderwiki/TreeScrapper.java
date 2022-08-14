package org.thunderwiki;

import org.database.dtos.VehicleInfo;
import org.enums.Nation;
import org.enums.VehicleType;

import java.util.List;

public interface TreeScrapper {
    List<VehicleInfo> getVehiclesInfo(Nation nation, VehicleType type);

    List<VehicleInfo> getVehiclesInfo(Nation nation);

    List<VehicleInfo> getVehiclesInfo(VehicleType type);

    List<VehicleInfo> getAllVehiclesInfo();

}
