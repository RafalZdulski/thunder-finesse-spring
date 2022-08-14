package org.thunderfinesse.services;

import org.database.dtos.PlayerModes;
import org.database.dtos.playerVehicleStats.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;
import org.thunderskill.NoSuchPlayerException;

import java.util.List;

public interface PlayerService {
    List<PlayerVehicleStats> getPlayerVehiclesStats(String login, Modes mode, VehicleType type) throws NoSuchPlayerException;

    List<PlayerModes> getPlayerModesStats(String login);

    /**updates all player statistics
     * @param login
     * @return true if player was successfully updated
     */
    boolean update(String login);
}
