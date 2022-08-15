package org.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.database.dtos.VehicleInfo;

@AllArgsConstructor
@NoArgsConstructor
public class VehicleStatsResponse {
    @Setter @Getter
    private VehicleInfo vehicle;
    @Setter @Getter
    private VehicleModeStats arcade;
    @Setter @Getter
    private VehicleModeStats realistic;
    @Setter @Getter
    private VehicleModeStats simulation;

}


