package org.thunderskill;

import org.dtos.playerVehicleStatsTables.PlayerVehicleStats;
import org.enums.Modes;
import org.enums.VehicleType;

import java.io.IOException;
import java.util.List;

public interface ThunderskillPlayerScrapper {
    List<PlayerVehicleStats> getPlayerStats(String login, Modes mode, VehicleType type) throws IOException, NoSuchPlayerException;

//    boolean checkIfPlayerExists(String login);
}
