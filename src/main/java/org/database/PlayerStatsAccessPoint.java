package org.database;

import org.database.dtos.Player;
import org.database.dtos.PlayerModes;
import org.database.dtos.playerVehicleStats.PlayerVehicleStats;
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
    PlayerModes getPlayerMode(String login, Modes mode, VehicleType type);

    List<PlayerVehicleStats> getVehicleStatList(String vehicleId, Modes mode, VehicleType type);
}
