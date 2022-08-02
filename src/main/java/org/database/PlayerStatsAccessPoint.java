package org.database;

import org.dtos.Player;
import org.dtos.PlayerModes;
import org.dtos.VehicleStats;
import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import java.util.List;

public interface PlayerStatsAccessPoint {
    void savePlayerVehicleStats(PlayerVehicleStats vehicle);
    void updatePlayerVehicleStats(PlayerVehicleStats vehicle);
    void upsertPlayerVehicleStats(PlayerVehicleStats vehicle);

    void savePlayer(Player player) throws PlayerAlreadyInDatabaseException;
    Player getPlayer(String login);
    List<PlayerVehicleStats> getPlayerStats(Player player, Modes mode, VehicleType type);
    void deletePlayer(String login);

    void savePlayerMode(PlayerModes playerMode);
    void updatePlayerMode(PlayerModes playerMode);
    void upsertPlayerMode(PlayerModes playerMode);

    List<PlayerModes> getPlayerModes(String login);

    List<PlayerVehicleStats> getVehicleStatList(String vehicleId, Modes mode, VehicleType type);
}
