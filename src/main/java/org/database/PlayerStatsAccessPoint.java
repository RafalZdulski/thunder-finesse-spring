package org.database;

import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;

public interface PlayerStatsAccessPoint {
    void savePlayerVehicleStats(PlayerVehicleStats vehicle);

    void updatePlayerVehicleStats(PlayerVehicleStats vehicle);

    void upsertPlayerVehicleStats(PlayerVehicleStats vehicle);


}
